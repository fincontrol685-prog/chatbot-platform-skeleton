export type BotPrimaryChannel = 'site' | 'whatsapp' | 'email' | 'instagram' | 'interno';
export type BotTone = 'profissional' | 'consultivo' | 'acolhedor' | 'objetivo';
export type BotResponseStyle = 'conciso' | 'equilibrado' | 'detalhado';

export interface BotProfessionalConfig {
  profile: {
    assistantRole: string;
    department: string;
    targetAudience: string;
    language: string;
    primaryChannel: BotPrimaryChannel;
    tone: BotTone;
    responseStyle: BotResponseStyle;
  };
  messages: {
    welcome: string;
    fallback: string;
    escalation: string;
    closing: string;
    offline: string;
    signature: string;
  };
  guidelines: {
    businessHours: string;
    firstResponseSlaMinutes: number;
    collectLead: boolean;
    requestProtocol: boolean;
    escalateSensitiveCases: boolean;
    allowEmoji: boolean;
  };
  knowledge: {
    scope: string;
    restrictions: string;
    successCriteria: string;
    requiredContext: string;
    handoffContext: string;
  };
  notes: string;
}

export interface BotPresetDefinition {
  id: string;
  label: string;
  badge: string;
  description: string;
  config: BotProfessionalConfig;
}

export interface BotConfigSummary {
  role: string;
  department: string;
  primaryChannel: string;
  primaryChannelLabel: string;
  tone: string;
  toneLabel: string;
  responseStyleLabel: string;
  welcomePreview: string;
  businessHours: string;
  serviceLevel: string;
  signature: string;
  messagePackComplete: boolean;
  isStructured: boolean;
}

export const BOT_CHANNEL_LABELS: Record<BotPrimaryChannel, string> = {
  site: 'Site',
  whatsapp: 'WhatsApp',
  email: 'Email',
  instagram: 'Instagram',
  interno: 'Canal interno'
};

export const BOT_TONE_LABELS: Record<BotTone, string> = {
  profissional: 'Profissional',
  consultivo: 'Consultivo',
  acolhedor: 'Acolhedor',
  objetivo: 'Objetivo'
};

export const BOT_RESPONSE_STYLE_LABELS: Record<BotResponseStyle, string> = {
  conciso: 'Conciso',
  equilibrado: 'Equilibrado',
  detalhado: 'Detalhado'
};

export const DEFAULT_BOT_CONFIG: BotProfessionalConfig = {
  profile: {
    assistantRole: 'Especialista de atendimento',
    department: 'Operacoes digitais',
    targetAudience: 'Clientes da operacao',
    language: 'pt-BR',
    primaryChannel: 'whatsapp',
    tone: 'profissional',
    responseStyle: 'equilibrado'
  },
  messages: {
    welcome: 'Ola! Sou a assistente virtual do time. Posso te ajudar com orientacoes, triagem e direcionamento rapido.',
    fallback: 'Nao identifiquei sua necessidade por completo. Se preferir, descreva o caso com mais detalhes para eu seguir.',
    escalation: 'Vou encaminhar seu atendimento para um especialista humano e registrar o contexto para acelerar a resposta.',
    closing: 'Atendimento concluido. Se precisar de algo mais, fico a disposicao.',
    offline: 'No momento estamos fora do horario de atendimento. Assim que o time voltar, sua solicitacao sera priorizada.',
    signature: 'Equipe de atendimento'
  },
  guidelines: {
    businessHours: 'Seg a Sex, 08:00 as 18:00',
    firstResponseSlaMinutes: 5,
    collectLead: true,
    requestProtocol: true,
    escalateSensitiveCases: true,
    allowEmoji: false
  },
  knowledge: {
    scope: 'Catalogo de servicos, status de solicitacoes e duvidas frequentes.',
    restrictions: 'Nao prometer excecoes comerciais sem aprovacao e nao compartilhar dados sensiveis.',
    successCriteria: 'Responder com clareza, registrar contexto e direcionar o cliente sem retrabalho.',
    requiredContext: 'objetivo da solicitacao; contexto atual; impacto percebido; urgencia',
    handoffContext: 'resumo do caso; impacto atual; prioridade; retorno esperado'
  },
  notes: ''
};

export const BOT_PRESETS: BotPresetDefinition[] = [
  {
    id: 'support-premium',
    label: 'Suporte premium',
    badge: 'Mais usado',
    description: 'Fluxo de suporte com triagem clara, SLA curto e linguagem profissional.',
    config: {
      profile: {
        assistantRole: 'Analista virtual de suporte',
        department: 'Suporte premium',
        targetAudience: 'Clientes com demandas operacionais e tecnicas',
        language: 'pt-BR',
        primaryChannel: 'whatsapp',
        tone: 'profissional',
        responseStyle: 'equilibrado'
      },
      messages: {
        welcome: 'Ola! Sou a assistente de suporte premium. Posso validar sua demanda e organizar o proximo passo com agilidade.',
        fallback: 'Para eu seguir com seguranca, preciso de mais contexto. Informe o que aconteceu, quando ocorreu e qual impacto voce percebeu.',
        escalation: 'Seu caso exige analise especializada. Vou encaminhar para o time responsavel com todo o historico registrado.',
        closing: 'Chamado registrado com sucesso. Seguiremos acompanhando sua solicitacao ate a conclusao.',
        offline: 'Estamos fora do horario prioritario neste momento, mas sua demanda ja foi registrada para retorno assim que a operacao reabrir.',
        signature: 'Central de suporte premium'
      },
      guidelines: {
        businessHours: 'Todos os dias, 07:00 as 22:00',
        firstResponseSlaMinutes: 3,
        collectLead: true,
        requestProtocol: true,
        escalateSensitiveCases: true,
        allowEmoji: false
      },
      knowledge: {
        scope: 'Incidentes, indisponibilidade, orientacoes de uso, segunda via e acompanhamento de chamados.',
        restrictions: 'Nao resetar credenciais criticas sem validacao e nao confirmar indisponibilidade sem evidencias basicas.',
        successCriteria: 'Reduzir tempo de triagem e entregar contexto completo para o especialista.',
        requiredContext: 'sistema ou fluxo afetado; erro observado; inicio do problema; impacto na operacao',
        handoffContext: 'resumo do incidente; evidencias coletadas; impacto atual; proximo passo esperado'
      },
      notes: ''
    }
  },
  {
    id: 'sales-consulting',
    label: 'Vendas consultivas',
    badge: 'Conversao',
    description: 'Abordagem mais consultiva para captar contexto comercial e gerar oportunidade.',
    config: {
      profile: {
        assistantRole: 'Consultor virtual comercial',
        department: 'Pre-vendas',
        targetAudience: 'Leads e clientes em avaliacao de proposta',
        language: 'pt-BR',
        primaryChannel: 'site',
        tone: 'consultivo',
        responseStyle: 'detalhado'
      },
      messages: {
        welcome: 'Ola! Posso entender seu contexto, indicar a melhor solucao e conectar voce ao time comercial certo.',
        fallback: 'Para sugerir a proposta ideal, me conte objetivo, porte da operacao e prazo desejado.',
        escalation: 'Vou transferir sua oportunidade para um consultor comercial com o resumo do que voce informou.',
        closing: 'Obrigado pelas informacoes. Nosso time dara continuidade com uma recomendacao aderente ao seu cenario.',
        offline: 'Recebemos seu interesse fora do horario comercial. Um consultor vai retomar assim que o time estiver online.',
        signature: 'Squad comercial'
      },
      guidelines: {
        businessHours: 'Seg a Sex, 09:00 as 19:00',
        firstResponseSlaMinutes: 10,
        collectLead: true,
        requestProtocol: false,
        escalateSensitiveCases: true,
        allowEmoji: false
      },
      knowledge: {
        scope: 'Qualificacao de lead, planos, demonstracoes, prazo de implantacao e proximos passos.',
        restrictions: 'Nao prometer descontos fora da politica e nao assumir integracoes sem avaliacao tecnica.',
        successCriteria: 'Qualificar o lead com dados completos para acelerar a conversao.',
        requiredContext: 'objetivo de negocio; porte da operacao; prazo de decisao; principais dores atuais',
        handoffContext: 'resumo da oportunidade; potencial de negocio; prazo; proxima acao comercial'
      },
      notes: ''
    }
  },
  {
    id: 'internal-helpdesk',
    label: 'Help desk interno',
    badge: 'Operacao',
    description: 'Bot interno para equipes, com respostas objetivas e foco em encaminhamento correto.',
    config: {
      profile: {
        assistantRole: 'Assistente interno de servicos',
        department: 'Service desk',
        targetAudience: 'Colaboradores e liderancas internas',
        language: 'pt-BR',
        primaryChannel: 'interno',
        tone: 'objetivo',
        responseStyle: 'conciso'
      },
      messages: {
        welcome: 'Ola! Posso registrar sua solicitacao interna, indicar politica vigente ou direcionar para o time correto.',
        fallback: 'Nao encontrei a categoria ideal ainda. Informe sistema, urgencia e impacto para eu classificar melhor.',
        escalation: 'Essa demanda sera enviada para a fila especializada com o resumo necessario para atendimento.',
        closing: 'Solicitacao registrada. Se houver mudanca de prioridade, atualize o chamado por este canal.',
        offline: 'A central interna esta fora do horario padrao. Sua solicitacao sera analisada no proximo ciclo util.',
        signature: 'Service desk corporativo'
      },
      guidelines: {
        businessHours: 'Seg a Sex, 08:00 as 18:00',
        firstResponseSlaMinutes: 15,
        collectLead: false,
        requestProtocol: true,
        escalateSensitiveCases: true,
        allowEmoji: false
      },
      knowledge: {
        scope: 'Acessos, sistemas internos, onboarding, equipamentos e politicas operacionais.',
        restrictions: 'Nao liberar permissao sem aprovacao e nao tratar incidente critico fora do fluxo oficial.',
        successCriteria: 'Abrir chamados consistentes e reduzir encaminhamentos incorretos.',
        requiredContext: 'colaborador afetado; sistema ou recurso; urgencia; impacto no trabalho',
        handoffContext: 'resumo da solicitacao; bloqueio atual; prioridade; area responsavel esperada'
      },
      notes: ''
    }
  }
];

export function cloneDefaultBotConfig(): BotProfessionalConfig {
  return JSON.parse(JSON.stringify(DEFAULT_BOT_CONFIG)) as BotProfessionalConfig;
}

export function parseBotConfig(rawConfig?: string | null): BotProfessionalConfig {
  if (!rawConfig?.trim()) {
    return cloneDefaultBotConfig();
  }

  try {
    return mergeBotConfig(JSON.parse(rawConfig) as Partial<BotProfessionalConfig>);
  } catch {
    const fallback = cloneDefaultBotConfig();
    fallback.notes = rawConfig.trim();
    return fallback;
  }
}

export function stringifyBotConfig(config: BotProfessionalConfig): string {
  return JSON.stringify(mergeBotConfig(config), null, 2);
}

export function buildBotConfigSummary(rawConfig?: string | null): BotConfigSummary {
  if (!rawConfig?.trim()) {
    return buildLegacySummary();
  }

  let config: BotProfessionalConfig;
  try {
    config = mergeBotConfig(JSON.parse(rawConfig) as Partial<BotProfessionalConfig>);
  } catch {
    return buildLegacySummary(rawConfig);
  }

  const welcomePreview = truncateText(config.messages.welcome, 120);
  const serviceLevel = config.guidelines.firstResponseSlaMinutes > 0
    ? `${config.guidelines.firstResponseSlaMinutes} min`
    : 'Nao definido';

  return {
    role: config.profile.assistantRole,
    department: config.profile.department,
    primaryChannel: config.profile.primaryChannel,
    primaryChannelLabel: BOT_CHANNEL_LABELS[config.profile.primaryChannel],
    tone: config.profile.tone,
    toneLabel: BOT_TONE_LABELS[config.profile.tone],
    responseStyleLabel: BOT_RESPONSE_STYLE_LABELS[config.profile.responseStyle],
    welcomePreview,
    businessHours: config.guidelines.businessHours || 'Horario nao informado',
    serviceLevel,
    signature: config.messages.signature || 'Equipe responsavel',
    messagePackComplete: Boolean(
      config.messages.welcome &&
      config.messages.fallback &&
      config.messages.escalation &&
      config.messages.closing
    ),
    isStructured: true
  };
}

export function splitConfigChecklist(value?: string | null): string[] {
  if (!value?.trim()) {
    return [];
  }

  return value
    .split(/\r?\n|;/)
    .map(item => item.trim())
    .filter(Boolean);
}

function mergeBotConfig(config?: Partial<BotProfessionalConfig> | null): BotProfessionalConfig {
  const base = cloneDefaultBotConfig();
  const safeConfig = isObject(config) ? config : {};

  return {
    profile: {
      ...base.profile,
      ...(isObject(safeConfig.profile) ? safeConfig.profile : {})
    },
    messages: {
      ...base.messages,
      ...(isObject(safeConfig.messages) ? safeConfig.messages : {})
    },
    guidelines: {
      ...base.guidelines,
      ...(isObject(safeConfig.guidelines) ? safeConfig.guidelines : {})
    },
    knowledge: {
      ...base.knowledge,
      ...(isObject(safeConfig.knowledge) ? safeConfig.knowledge : {})
    },
    notes: typeof safeConfig.notes === 'string' ? safeConfig.notes : base.notes
  };
}

function truncateText(value: string, maxLength: number): string {
  if (!value || value.length <= maxLength) {
    return value;
  }

  return `${value.slice(0, maxLength - 3).trim()}...`;
}

function buildLegacySummary(rawConfig?: string): BotConfigSummary {
  return {
    role: 'Perfil nao configurado',
    department: 'Revisar configuracao',
    primaryChannel: '',
    primaryChannelLabel: 'Nao configurado',
    tone: '',
    toneLabel: 'Nao configurado',
    responseStyleLabel: 'Padrao',
    welcomePreview: rawConfig?.trim()
      ? truncateText(rawConfig.trim(), 120)
      : 'Sem biblioteca de mensagens estruturada.',
    businessHours: 'Horario nao informado',
    serviceLevel: 'Nao definido',
    signature: 'Time responsavel',
    messagePackComplete: false,
    isStructured: false
  };
}

function isObject(value: unknown): value is Record<string, unknown> {
  return typeof value === 'object' && value !== null && !Array.isArray(value);
}
