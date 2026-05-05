import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/auth.service';
import { getApiErrorMessage } from '../../../core/api-error.util';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css'],
    standalone: false
})
export class RegisterComponent {
  form: FormGroup;
  loading = false;
  error = '';
  success = '';

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', Validators.required]
    });
  }

  submit(): void {
    this.error = '';
    this.success = '';
    if (this.form.invalid) {
      this.error = 'Preencha todos os campos corretamente.';
      return;
    }
    const { username, email, password, confirmPassword } = this.form.value;
    if (password !== confirmPassword) {
      this.error = 'As senhas não conferem.';
      return;
    }
    this.loading = true;
    this.auth.register(username, email, password).subscribe({
      next: () => {
        this.loading = false;
        this.success = 'Conta criada com sucesso! Redirecionando...';
        setTimeout(() => void this.router.navigate(['/dashboard']), 1000);
      },
      error: (err) => {
        this.loading = false;
        this.error = getApiErrorMessage(err, 'Falha ao criar conta. Tente novamente.');
        // eslint-disable-next-line no-console
        console.error('Register error:', err);
      }
    });
  }
}
