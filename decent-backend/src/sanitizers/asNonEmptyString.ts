import { withErrorMessage, asMatching } from '@restless/sanitizers'

export const asNonEmptyString = withErrorMessage(asMatching(/.+/), 'non-empty string');
