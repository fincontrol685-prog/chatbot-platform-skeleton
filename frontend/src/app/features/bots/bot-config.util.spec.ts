import { buildBotConfigSummary, parseBotConfig, stringifyBotConfig } from './bot-config.util';

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
});
