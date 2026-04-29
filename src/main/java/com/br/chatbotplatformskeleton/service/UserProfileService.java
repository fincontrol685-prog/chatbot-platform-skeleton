package com.br.chatbotplatformskeleton.service;

import com.br.chatbotplatformskeleton.domain.Department;
import com.br.chatbotplatformskeleton.domain.Role;
import com.br.chatbotplatformskeleton.domain.UserAccount;
import com.br.chatbotplatformskeleton.dto.RoleOptionDto;
import com.br.chatbotplatformskeleton.dto.UserProfileDto;
import com.br.chatbotplatformskeleton.dto.UserUpsertRequest;
import com.br.chatbotplatformskeleton.repository.DepartmentRepository;
import com.br.chatbotplatformskeleton.repository.RoleRepository;
import com.br.chatbotplatformskeleton.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@Transactional
public class UserProfileService {

    private static final String SYSTEM_USERNAME = "bot.system";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    public UserProfileService(
        UserRepository userRepository,
        RoleRepository roleRepository,
        DepartmentRepository departmentRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UserProfileDto> listUsers(String search, Boolean enabled) {
        String normalizedSearch = search == null ? "" : search.trim().toLowerCase(Locale.ROOT);

        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
            .stream()
            .filter(user -> !SYSTEM_USERNAME.equalsIgnoreCase(user.getUsername()))
            .filter(user -> enabled == null || enabled.equals(Boolean.TRUE.equals(user.getEnabled())))
            .filter(user -> normalizedSearch.isBlank() || matchesSearch(user, normalizedSearch))
            .map(this::toDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public UserProfileDto getUser(Long id) {
        UserAccount user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));
        return toDto(user);
    }

    @Transactional(readOnly = true)
    public List<RoleOptionDto> listRoles() {
        return roleRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
            .stream()
            .map(role -> {
                RoleOptionDto dto = new RoleOptionDto();
                dto.setName(role.getName());
                dto.setDescription(role.getDescription());
                return dto;
            })
            .toList();
    }

    public UserProfileDto createUser(UserUpsertRequest request) {
        String username = normalizeUsername(request.getUsername());
        String email = normalizeEmail(request.getEmail());
        String password = normalizePassword(request.getPassword(), true);

        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new IllegalArgumentException("Usuario ja cadastrado");
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Email ja cadastrado");
        }

        UserAccount user = new UserAccount();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setEnabled(request.getEnabled() == null || request.getEnabled());
        user.setRoles(resolveRoles(request.getRoles()));
        user.setDepartments(resolveDepartments(request.getDepartmentIds()));

        return toDto(userRepository.save(user));
    }

    public UserProfileDto updateUser(Long id, UserUpsertRequest request) {
        UserAccount user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));

        String username = normalizeUsername(request.getUsername());
        String email = normalizeEmail(request.getEmail());

        userRepository.findByUsernameIgnoreCase(username)
            .filter(existing -> !existing.getId().equals(id))
            .ifPresent(existing -> {
                throw new IllegalArgumentException("Usuario ja cadastrado");
            });

        userRepository.findByEmailIgnoreCase(email)
            .filter(existing -> !existing.getId().equals(id))
            .ifPresent(existing -> {
                throw new IllegalArgumentException("Email ja cadastrado");
            });

        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(request.getEnabled() == null || request.getEnabled());
        user.setRoles(resolveRoles(request.getRoles()));
        user.setDepartments(resolveDepartments(request.getDepartmentIds()));

        String password = request.getPassword() == null ? "" : request.getPassword().trim();
        if (!password.isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(normalizePassword(password, false)));
        }

        return toDto(userRepository.save(user));
    }

    public UserProfileDto updateStatus(Long id, boolean enabled) {
        UserAccount user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));
        user.setEnabled(enabled);
        return toDto(userRepository.save(user));
    }

    private boolean matchesSearch(UserAccount user, String search) {
        if (safeValue(user.getUsername()).contains(search) || safeValue(user.getEmail()).contains(search)) {
            return true;
        }

        boolean matchesRole = user.getRoles().stream()
            .map(Role::getName)
            .map(this::safeValue)
            .anyMatch(value -> value.contains(search));

        if (matchesRole) {
            return true;
        }

        return user.getDepartments().stream()
            .map(Department::getName)
            .map(this::safeValue)
            .anyMatch(value -> value.contains(search));
    }

    private Set<Role> resolveRoles(Set<String> requestedRoles) {
        Set<String> normalizedRoles = requestedRoles == null || requestedRoles.isEmpty()
            ? Set.of("USUARIO")
            : requestedRoles.stream()
                .filter(value -> value != null && !value.isBlank())
                .map(value -> value.trim().toUpperCase(Locale.ROOT))
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));

        Set<Role> roles = new LinkedHashSet<>();
        for (String roleName : normalizedRoles) {
            Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Perfil " + roleName + " nao encontrado"));
            roles.add(role);
        }
        return roles;
    }

    private Set<Department> resolveDepartments(Set<Long> requestedDepartmentIds) {
        if (requestedDepartmentIds == null || requestedDepartmentIds.isEmpty()) {
            return new LinkedHashSet<>();
        }

        Set<Long> ids = requestedDepartmentIds.stream()
            .filter(java.util.Objects::nonNull)
            .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));

        List<Department> departments = departmentRepository.findAllById(ids);
        if (departments.size() != ids.size()) {
            throw new IllegalArgumentException("Um ou mais departamentos nao foram encontrados");
        }

        return new LinkedHashSet<>(departments);
    }

    private UserProfileDto toDto(UserAccount user) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setDisplayName(toDisplayName(user.getUsername()));
        dto.setEmail(user.getEmail());
        dto.setEnabled(Boolean.TRUE.equals(user.getEnabled()));
        dto.setCreatedAt(user.getCreatedAt());
        dto.setRoles(user.getRoles().stream()
            .map(Role::getName)
            .sorted()
            .toList());
        dto.setDepartmentIds(user.getDepartments().stream()
            .map(Department::getId)
            .sorted(Comparator.nullsLast(Long::compareTo))
            .toList());
        dto.setDepartmentNames(user.getDepartments().stream()
            .map(Department::getName)
            .sorted()
            .toList());
        return dto;
    }

    private String normalizeUsername(String username) {
        String normalized = normalizeRequired(username, "Usuario");
        if (SYSTEM_USERNAME.equalsIgnoreCase(normalized)) {
            throw new IllegalArgumentException("Usuario reservado para uso interno");
        }
        return normalized;
    }

    private String normalizeEmail(String email) {
        return normalizeRequired(email, "Email").toLowerCase(Locale.ROOT);
    }

    private String normalizePassword(String password, boolean required) {
        String normalized = password == null ? "" : password.trim();
        if (required && normalized.isBlank()) {
            throw new IllegalArgumentException("Senha e obrigatoria");
        }
        if (!normalized.isBlank() && normalized.length() < 8) {
            throw new IllegalArgumentException("Senha deve ter no minimo 8 caracteres");
        }
        return normalized;
    }

    private String normalizeRequired(String value, String fieldName) {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException(fieldName + " invalido");
        }
        return value.trim();
    }

    private String safeValue(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }

    private String toDisplayName(String username) {
        String label = username == null ? "" : username.replaceAll("[._-]+", " ").trim();
        if (label.isBlank()) {
            return "Operador";
        }

        return java.util.Arrays.stream(label.split(" "))
            .filter(part -> !part.isBlank())
            .map(part -> part.substring(0, 1).toUpperCase(Locale.ROOT) + part.substring(1).toLowerCase(Locale.ROOT))
            .reduce((first, second) -> first + " " + second)
            .orElse("Operador");
    }
}
