export class InputSanitizer {

  // 🟢 Combined XSS + SQL injection detection regex
  private static attackRegex = new RegExp(
    [
      // --- XSS Patterns ---
      "<\\s*script\\b[^>]*>?",
      "<\\s*/\\s*script\\b[^>]*>?",
      "<\\s*img\\b[^>]*on\\w+\\s*=",
      "<\\s*svg\\b[^>]*on\\w+\\s*=",
      "on\\w+\\s*=",                    // onload=, onclick=
      "javascript\\s*:",
      "document\\.cookie",
      "window\\.location",
      "document\\.write",
      "eval\\(",

      // --- SQL Injection Patterns ---
      "'\\s*or\\s*'1'='1",
      "\"\\s*or\\s*\"1\"=\"1",
      "--",                            // SQL comment
      ";\\s*waitfor\\s+delay",
      "'\\s*--",
      "1=1",
      "union\\s+select",
      "drop\\s+table",
      "insert\\s+into",
      "delete\\s+from",
            "update\\s+table",

    ].join("|"),
    "i"
  );

  /**
   * Checks input for XSS or SQL Injection attempts
   * @param value - the string to validate
   * @returns boolean - true if invalid/malicious
   */
  static isInvalid(value: string): boolean {
    if (!value) return false;
    return this.attackRegex.test(value);
  }
}
