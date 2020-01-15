import {SanitizeError} from '@restless/restless'
import {Request, Response, NextFunction} from 'express'
import {HttpAwareError, InvalidRequestError, UnknownServerError} from '../domain/errors';

function preprocess(error: unknown) {
  if (error instanceof HttpAwareError) {
    return error;
  }

  if (error instanceof SanitizeError) {
    return new InvalidRequestError(error.errors);
  }

  return new UnknownServerError('An unknown error occurred');
}

export function errorHandler(err: unknown, req: Request, res: Response, next: NextFunction) {
  const error = preprocess(err);
  res.status(error.status).json(error.toJSON());
}
