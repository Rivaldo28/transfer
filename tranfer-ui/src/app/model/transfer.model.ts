export interface Transfer {
  id?: number;
  accountOrigin: string;
  accountDestination: string;
  transferValue: number;
  rate: number;
  notice?: string;
  dateTransfer: string;
  dateScheduling: string | null;
}