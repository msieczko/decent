import {SanitizerFailure} from '@restless/sanitizers'

export class HttpAwareError extends Error {
  constructor(
    message: string,
    public readonly type: string,
    public readonly status: number,
  ) {
    super(message)
  }

  toJSON() {
    return {
      status: this.status,
      type: this.type,
      message: this.message,
    }
  }
}

export class InvalidRequestError extends HttpAwareError {
  constructor(
    public errors: SanitizerFailure[],
  ) {
    super('Request contains invalid data', 'INVALID_REQUEST', 400)
  }

  toJSON() {
    return {
      ...super.toJSON(),
      errors: this.errors,
    }
  }
}

export class UnknownServerError extends HttpAwareError {
  constructor (message: string) {
    super(message, 'UNKNOWN_SERVER_ERROR', 500)
  }
}
