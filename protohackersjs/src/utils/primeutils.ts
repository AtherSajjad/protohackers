export function checkIsPrime(input: number): boolean {
  if (input <= 1) {
    return false;
  }

  for (let i = 2; i <= Math.sqrt(input); i++) {
    if (input % i == 0) {
      return false;
    }
  }

  return true;
}
