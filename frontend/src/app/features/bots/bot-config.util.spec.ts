import { buildBotConfigSummary, parseBotConfig, splitConfigChecklist, stringifyBotConfig } from './bot-config.util';

describe('bot-config util', () => {
  it('should preserve legacy notes when config is not valid json', () => {
    const config = parseBotConfig('Observacao legada');

    expect(config.notes).toBe('Observacao legada');
    expect(config.profile.assistantRole).toBeTruthy();
  });

  it('should build a complete summary from structured config', () => {
    const rawConfig = stringifyBotConfig(parseBotConfig(null));
    const summary = buildBotConfigSummary(rawConfig);

    expect(summary.primaryChannelLabel).toBeTruthy();
    expect(summary.messagePackComplete).toBeTrue();
    expect(summary.welcomePreview.length).toBeGreaterThan(0);
  });

  it('should keep structured context fields when serializing config', () => {
    const config = parseBotConfig(null);
    config.knowledge.requiredContext = 'sistema afetado; impacto; urgencia';
    config.knowledge.handoffContext = 'resumo; prioridade; retorno esperado';

    const parsedAgain = parseBotConfig(stringifyBotConfig(config));

    expect(parsedAgain.knowledge.requiredContext).toContain('impacto');
    expect(parsedAgain.knowledge.handoffContext).toContain('retorno esperado');
  });

  it('should split checklist items by semicolon or line break', () => {
    const items = splitConfigChecklist('sistema afetado; impacto atual\nurgencia');

    expect(items).toEqual(['sistema afetado', 'impacto atual', 'urgencia']);
  });
});
