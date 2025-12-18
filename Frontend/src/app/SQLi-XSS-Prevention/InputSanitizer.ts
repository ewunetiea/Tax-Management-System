// export class InputSanitizer {

//   /**
//    * BLOCKLIST REGEX
//    * Detects XSS + SQLi + HTML injection patterns
//    */
//   public static attackRegex = new RegExp(
//     [
//       // ----------------------------
//       // 🚨 XSS ATTACKS
//       // ----------------------------
//       "<\\s*script\\b",
//       "<\\s*/\\s*script\\b",
//       "<\\s*img[^>]+on\\w+\\s*=",
//       "<\\s*svg[^>]+on\\w+\\s*=",
//       "<\\s*iframe\\b",
//       "<\\s*object\\b",
//       "<\\s*embed\\b",
//       "<\\s*link[^>]*stylesheet",
//       "on\\w+\\s*=",          // onclick= onload=
//       "javascript\\s*:",
//       "data\\s*:\\s*text\\/html",
//       "vbscript\\s*:",
//       "document\\.",
//       "window\\.",
//       "eval\\(",
//       "alert\\(",
//       "prompt\\(",
//       "confirm\\(",

//       // Unicode-hidden payloads
//       "[\\u202E\\u202D\\u202B\\u202A]",   // RTL override chars

//       // ----------------------------
//       // 🚨 SQL INJECTION
//       // ----------------------------
//       "'\\s*or\\s*'1'='1",
//       "\"\\s*or\\s*\"1\"=\"1",
//       "--",                // SQL comments
//       ";\\s*waitfor\\s+delay",
//       "union\\s+select",
//       "drop\\s+table",
//       "insert\\s+into",
//       "delete\\s+from",
//       "update\\s+.*set",
//       "select\\s+.*from",
//       "exec\\s*\\(",
//       "declare\\s+@",
//       "cast\\s*\\(",
//       "alter\\s+table",
//       "truncate\\s+table",
//       "information_schema",
//       "xp_cmdshell",
//       "limit\\s+\\d+",
//       "offset\\s+\\d+",

//       // ----------------------------
//       // 🚨 HTML / BROWSER-ENGINE EXPLOITS
//       // ----------------------------
//       "<\\s*form\\b[^>]*action",
//       "<\\s*meta\\b[^>]*http-equiv",
//       "<\\s*base\\b",
//       "<\\s*style\\b",
//     ].join("|"),
//     "i"
//   );

//   /**
//    * Detects malicious input without modifying it
//    */
//   static isInvalid(value: string): boolean {
//     if (!value) return false;
//     return this.attackRegex.test(value);
//   }

//   /**
//    * SANITIZER FUNCTION
//    * Safely removes HTML + risky keywords while keeping normal text intact
//    */
//   static cleanInput(value: string): string {
//     if (!value) return "";

//     let cleaned = value;

//     // Strip ALL HTML tags
//     cleaned = cleaned.replace(/<[^>]*>/g, "");

//     // Remove unicode-direction attacks
//     cleaned = cleaned.replace(/[\u202E\u202D\u202B\u202A]/g, "");

//     // Remove common XSS JS words
//     cleaned = cleaned.replace(/javascript|vbscript|script|eval|alert|prompt|confirm/gi, "");

//     // Remove SQL keywords
//     cleaned = cleaned.replace(/\b(select|insert|delete|update|union|drop|truncate|exec|cast|declare)\b/gi, "");

//     // Collapse multiple spaces
//     cleaned = cleaned.replace(/\s\s+/g, " ").trim();

//     return cleaned;
//   }
// }


export class InputSanitizer {

  /**
   * BLOCKLIST REGEX
   * Detects XSS + SQLi + HTML injection patterns
   * UPDATED: Fixed to reliably catch 'OR '1'='1' variations including lowercase "or", no spaces, and gaps
   */
  public static attackRegex = new RegExp(
    [
      // ----------------------------
      // 🚨 XSS ATTACKS
      // ----------------------------
      "<\\s*script\\b",
      "<\\s*/\\s*script\\b",
      "<\\s*img[^>]+on\\w+\\s*=",
      "<\\s*svg[^>]+on\\w+\\s*=",
      "<\\s*iframe\\b",
      "<\\s*object\\b",
      "<\\s*embed\\b",
      "<\\s*link[^>]*stylesheet",
      "on\\w+\\s*=",          // onclick= onload=
      "javascript\\s*:",
      "data\\s*:\\s*text\\/html",
      "vbscript\\s*:",
      "document\\.",
      "window\\.",
      "eval\\(",
      "alert\\(",
      "prompt\\(",
      "confirm\\(",

      // Unicode-hidden payloads
      "[\\u202E\\u202D\\u202B\\u202A]",   // RTL override chars

      // ----------------------------
      // 🚨 SQL INJECTION - FULLY FIXED FOR ALL '1'='1' VARIATIONS
      // ----------------------------
      "'?\\s*or\\s*'?1'?\\s*=\\s*'?1'?",     // Core pattern: catches OR '1'='1', or'1'='1', 'or '1' = '1', or 1=1, etc.
      "\"?\\s*or\\s*\"?1\"?\\s*=\\s*\"?1\"?", // Same for double quotes
      "1\\s*=\\s*1",                         // Plain 1=1 or 1 = 1 (very common)

      "--",                                  // SQL comments
      ";\\s*waitfor\\s+delay",
      "union\\s+select",
      "drop\\s+table",
      "insert\\s+into",
      "delete\\s+from",
      "update\\s+.*set",
      "select\\s+.*from",
      "exec\\s*\\(",
      "declare\\s+@",
      "cast\\s*\\(",
      "alter\\s+table",
      "truncate\\s+table",
      "information_schema",
      "xp_cmdshell",
      "limit\\s+\\d+",
      "offset\\s+\\d+",

      // ----------------------------
      // 🚨 HTML / BROWSER-ENGINE EXPLOITS
      // ----------------------------
      "<\\s*form\\b[^>]*action",
      "<\\s*meta\\b[^>]*http-equiv",
      "<\\s*base\\b",
      "<\\s*style\\b",
    ].join("|"),
    "i"
  );

  /**
   * Detects malicious input without modifying it
   */
  static isInvalid(value: string): boolean {
    if (!value) return false;
    return this.attackRegex.test(value);
  }

  /**
   * SANITIZER FUNCTION
   * Safely removes HTML + risky keywords while keeping normal text intact
   */
  static cleanInput(value: string): string {
    if (!value) return "";

    let cleaned = value;

    // Strip ALL HTML tags
    cleaned = cleaned.replace(/<[^>]*>/g, "");

    // Remove unicode-direction attacks
    cleaned = cleaned.replace(/[\u202E\u202D\u202B\u202A]/g, "");

    // Remove common XSS JS words
    cleaned = cleaned.replace(/javascript|vbscript|script|eval|alert|prompt|confirm/gi, "");

    // Remove SQL keywords
    cleaned = cleaned.replace(/\b(select|insert|delete|update|union|drop|truncate|exec|cast|declare)\b/gi, "");

    // Collapse multiple spaces
    cleaned = cleaned.replace(/\s\s+/g, " ").trim();

    return cleaned;
  }
}