import { ChangeDetectionStrategy, ChangeDetectorRef, Component, DestroyRef, OnInit, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { getApiErrorMessage } from '../../../core/api-error.util';
import { BotService, BotDto } from '../bot.service';
import {
  BOT_CHANNEL_LABELS,
  BOT_PRESETS,
  BOT_RESPONSE_STYLE_LABELS,
  BOT_TONE_LABELS,
  DEFAULT_BOT_CONFIG,
  BotConfigSummary,
  BotPrimaryChannel,
  BotProfessionalConfig,
  BotResponseStyle,
  BotTone,
  buildBotConfigSummary,
  parseBotConfig,
  stringifyBotConfig
} from '../bot-config.util';

@Component({
  selector: 'app-bot-create',
  templateUrl: './bot-create.component.html',
  styleUrls: ['./bot-create.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class BotCreateComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly botService = inject(BotService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly cdr = inject(ChangeDetectorRef);

  readonly presets = BOT_PRESETS;
  readonly channelOptions = Object.entries(BOT_CHANNEL_LABELS).map(([value, label]) => ({
    value: value as BotPrimaryChannel,
    label
  }));
  readonly toneOptions = Object.entries(BOT_TONE_LABELS).map(([value, label]) => ({
    value: value as BotTone,
    label
  }));
  readonly responseStyleOptions = Object.entries(BOT_RESPONSE_STYLE_LABELS).map(([value, label]) => ({
    value: value as BotResponseStyle,
    label
  }));

  readonly form = this.fb.nonNullable.group({
    identity: this.fb.nonNullable.group({
      name: ['', [Validators.required, Validators.maxLength(120)]],
      key: ['', [Validators.required, Validators.maxLength(80), Validators.pattern(/^[a-zA-Z0-9_-]+$/)]],
      enabled: [true]
    }),
    profile: this.fb.nonNullable.group({
      assistantRole: [DEFAULT_BOT_CONFIG.profile.assistantRole, [Validators.required, Validators.maxLength(120)]],
      department: [DEFAULT_BOT_CONFIG.profile.department, [Validators.maxLength(120)]],
      targetAudience: [DEFAULT_BOT_CONFIG.profile.targetAudience, [Validators.maxLength(160)]],
      language: [DEFAULT_BOT_CONFIG.profile.language, [Validators.required, Validators.maxLength(20)]],
      primaryChannel: [DEFAULT_BOT_CONFIG.profile.primaryChannel, Validators.required],
      tone: [DEFAULT_BOT_CONFIG.profile.tone, Validators.required],
      responseStyle: [DEFAULT_BOT_CONFIG.profile.responseStyle, Validators.required]
    }),
    messages: this.fb.nonNullable.group({
      welcome: [DEFAULT_BOT_CONFIG.messages.welcome, [Validators.required, Validators.maxLength(500)]],
      fallback: [DEFAULT_BOT_CONFIG.messages.fallback, [Validators.required, Validators.maxLength(500)]],
      escalation: [DEFAULT_BOT_CONFIG.messages.escalation, [Validators.required, Validators.maxLength(500)]],
      closing: [DEFAULT_BOT_CONFIG.messages.closing, [Validators.required, Validators.maxLength(500)]],
      offline: [DEFAULT_BOT_CONFIG.messages.offline, [Validators.maxLength(500)]],
      signature: [DEFAULT_BOT_CONFIG.messages.signature, [Validators.maxLength(120)]]
    }),
    guidelines: this.fb.nonNullable.group({
      businessHours: [DEFAULT_BOT_CONFIG.guidelines.businessHours, [Validators.maxLength(120)]],
      firstResponseSlaMinutes: [
        DEFAULT_BOT_CONFIG.guidelines.firstResponseSlaMinutes,
        [Validators.min(0), Validators.max(1440)]
      ],
      collectLead: [DEFAULT_BOT_CONFIG.guidelines.collectLead],
      requestProtocol: [DEFAULT_BOT_CONFIG.guidelines.requestProtocol],
      escalateSensitiveCases: [DEFAULT_BOT_CONFIG.guidelines.escalateSensitiveCases],
      allowEmoji: [DEFAULT_BOT_CONFIG.guidelines.allowEmoji]
    }),
    knowledge: this.fb.nonNullable.group({
      scope: [DEFAULT_BOT_CONFIG.knowledge.scope, [Validators.maxLength(800)]],
      restrictions: [DEFAULT_BOT_CONFIG.knowledge.restrictions, [Validators.maxLength(800)]],
      successCriteria: [DEFAULT_BOT_CONFIG.knowledge.successCriteria, [Validators.maxLength(800)]]
    }),
    notes: ['']
  });

  loading = false;
  loadingBot = false;
  error = '';
  isEditMode = false;

  private botId: number | null = null;

  get identityGroup() {
    return this.form.controls.identity;
  }

  get profileGroup() {
    return this.form.controls.profile;
  }

  get messagesGroup() {
    return this.form.controls.messages;
  }

  get guidelinesGroup() {
    return this.form.controls.guidelines;
  }

  get knowledgeGroup() {
    return this.form.controls.knowledge;
  }

  get pageEyebrow(): string {
    return this.isEditMode ? 'Governanca operacional' : 'Provisionamento profissional';
  }

  get pageTitle(): string {
    return this.isEditMode ? 'Editar configuracao do bot' : 'Novo bot profissional';
  }

  get pageDescription(): string {
    return this.isEditMode
      ? 'Ajuste mensagens, tom de atendimento, regras operacionais e orientacoes internas do bot.'
      : 'Monte um bot com mensagens padronizadas, perfil de comunicacao e regras de atendimento prontas para operacao.';
  }

  get submitLabel(): string {
    return this.isEditMode ? 'Salvar configuracao' : 'Criar bot';
  }

  get previewSummary(): BotConfigSummary {
    return buildBotConfigSummary(stringifyBotConfig(this.buildConfigModel()));
  }

  get previewConfig(): BotProfessionalConfig {
    return this.buildConfigModel();
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (!idParam) {
      return;
    }

    const parsedId = Number(idParam);
    if (Number.isNaN(parsedId)) {
      this.error = 'Identificador do bot invalido.';
      return;
    }

    this.isEditMode = true;
    this.botId = parsedId;
    this.loadBot(parsedId);
  }

  applyPreset(presetId: string): void {
    const preset = this.presets.find(item => item.id === presetId);
    if (!preset) {
      return;
    }

    this.profileGroup.patchValue(preset.config.profile);
    this.messagesGroup.patchValue(preset.config.messages);
    this.guidelinesGroup.patchValue(preset.config.guidelines);
    this.knowledgeGroup.patchValue(preset.config.knowledge);
    this.form.controls.notes.setValue(preset.config.notes);

    if (!this.identityGroup.controls.name.value.trim()) {
      this.identityGroup.controls.name.setValue(preset.label);
    }

    if (!this.identityGroup.controls.key.value.trim()) {
      this.identityGroup.controls.key.setValue(this.buildSuggestedKey(preset.label));
    }
  }

  suggestKey(): void {
    const suggestedKey = this.buildSuggestedKey(this.identityGroup.controls.name.value);
    if (suggestedKey) {
      this.identityGroup.controls.key.setValue(suggestedKey);
    }
  }

  submit(): void {
    this.error = '';

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    const payload = this.buildPayload();
    const request = this.isEditMode && this.botId !== null
      ? this.botService.update(this.botId, payload)
      : this.botService.create(payload);

    request
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.loading = false;
          this.cdr.markForCheck();
          void this.router.navigate(['/bots']);
        },
        error: (errorResponse) => {
          this.loading = false;
          this.error = getApiErrorMessage(
            errorResponse,
            this.isEditMode ? 'Nao foi possivel salvar o bot.' : 'Nao foi possivel criar o bot.'
          );
          this.cdr.markForCheck();
          console.error(errorResponse);
        }
      });
  }

  private loadBot(id: number): void {
    this.loadingBot = true;
    this.error = '';

    this.botService.get(id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: bot => {
          this.patchForm(bot);
          this.loadingBot = false;
          this.cdr.markForCheck();
        },
        error: errorResponse => {
          this.loadingBot = false;
          this.error = getApiErrorMessage(errorResponse, 'Nao foi possivel carregar a configuracao do bot.');
          this.cdr.markForCheck();
          console.error(errorResponse);
        }
      });
  }

  private patchForm(bot: BotDto): void {
    const config = parseBotConfig(bot.config);

    this.form.patchValue({
      identity: {
        name: bot.name,
        key: bot.key,
        enabled: bot.enabled ?? true
      },
      profile: config.profile,
      messages: config.messages,
      guidelines: config.guidelines,
      knowledge: config.knowledge,
      notes: config.notes
    });
  }

  private buildPayload(): Partial<BotDto> {
    return {
      name: this.identityGroup.controls.name.value.trim(),
      key: this.identityGroup.controls.key.value.trim(),
      enabled: this.identityGroup.controls.enabled.value,
      config: stringifyBotConfig(this.buildConfigModel())
    };
  }

  private buildConfigModel(): BotProfessionalConfig {
    const rawValue = this.form.getRawValue();

    return {
      profile: {
        assistantRole: rawValue.profile.assistantRole.trim(),
        department: rawValue.profile.department.trim(),
        targetAudience: rawValue.profile.targetAudience.trim(),
        language: rawValue.profile.language.trim(),
        primaryChannel: rawValue.profile.primaryChannel,
        tone: rawValue.profile.tone,
        responseStyle: rawValue.profile.responseStyle
      },
      messages: {
        welcome: rawValue.messages.welcome.trim(),
        fallback: rawValue.messages.fallback.trim(),
        escalation: rawValue.messages.escalation.trim(),
        closing: rawValue.messages.closing.trim(),
        offline: rawValue.messages.offline.trim(),
        signature: rawValue.messages.signature.trim()
      },
      guidelines: {
        businessHours: rawValue.guidelines.businessHours.trim(),
        firstResponseSlaMinutes: Number(rawValue.guidelines.firstResponseSlaMinutes || 0),
        collectLead: rawValue.guidelines.collectLead,
        requestProtocol: rawValue.guidelines.requestProtocol,
        escalateSensitiveCases: rawValue.guidelines.escalateSensitiveCases,
        allowEmoji: rawValue.guidelines.allowEmoji
      },
      knowledge: {
        scope: rawValue.knowledge.scope.trim(),
        restrictions: rawValue.knowledge.restrictions.trim(),
        successCriteria: rawValue.knowledge.successCriteria.trim()
      },
      notes: rawValue.notes?.trim() ?? ''
    };
  }

  private buildSuggestedKey(value: string): string {
    return value
      .toLowerCase()
      .trim()
      .replace(/[^a-z0-9]+/g, '-')
      .replace(/^-+|-+$/g, '')
      .slice(0, 80);
  }
}
