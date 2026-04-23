import { HttpErrorResponse } from '@angular/common/http';

interface ValidationErrorPayload {
  field?: string;
  message?: string;
}

interface ApiErrorPayload {
  error?: string;
  message?: string;
  validationErrors?: ValidationErrorPayload[];
}

export function getApiErrorMessage(error: unknown, fallbackMessage: string): string {
  const httpError = error as HttpErrorResponse | undefined;
  const payload = httpError?.error as ApiErrorPayload | string | undefined;

  if (typeof payload === 'string' && payload.trim()) {
    return payload;
  }

  if (payload && typeof payload === 'object') {
    const validationErrors = Array.isArray(payload.validationErrors)
      ? payload.validationErrors
          .map(validationError => validationError?.message?.trim())
          .filter((message): message is string => !!message)
      : [];

    if (validationErrors.length > 0) {
      return validationErrors.join(' ');
    }

    if (payload.message?.trim()) {
      return payload.message;
    }

    if (payload.error?.trim()) {
      return payload.error;
    }
  }

  if (httpError?.message?.trim()) {
    return httpError.message;
  }

  return fallbackMessage;
}
