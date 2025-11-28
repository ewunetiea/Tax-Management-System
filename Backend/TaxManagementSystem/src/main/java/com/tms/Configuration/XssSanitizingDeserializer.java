// package com.tms.Configuration;

// import com.fasterxml.jackson.core.JsonParser;
// import com.fasterxml.jackson.databind.DeserializationContext;
// import com.fasterxml.jackson.databind.JsonDeserializer;
// import org.owasp.html.HtmlPolicyBuilder;
// import org.owasp.html.PolicyFactory;
// import org.apache.commons.text.StringEscapeUtils;
// import java.io.IOException;
// import java.util.regex.Pattern;

// public class XssSanitizingDeserializer extends JsonDeserializer<String> {

//     private static final Pattern XSS_PATTERN = Pattern.compile(
//         "(<\\s*script[^>]*>.*?<\\s*/\\s*script[^>]*>|javascript\\s*:|<\\s*\\w+\\s+\\w+\\s*=\\s*[^>]*\\s*\\w+\\s*=)",
//         Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL
//     );

//     private static final Pattern HTML_TAG_PATTERN = Pattern.compile(".*<[^>]+>.*");

//     private static final PolicyFactory HTML_SANITIZER = new HtmlPolicyBuilder()
//         .allowElements(
//             "a", "b", "blockquote", "br", "caption", "cite", "code", "col", "colgroup",
//             "dd", "div", "dl", "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6",
//             "i", "img", "li", "ol", "p", "pre", "q", "small", "span", "strong",
//             "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "ul"
//         )
//         .allowAttributes("href", "title", "target").onElements("a")
//         .requireRelNofollowOnLinks()
//         .allowAttributes("src", "alt", "title", "width", "height").onElements("img")
//         .allowAttributes("colspan", "rowspan").onElements("td", "th")
//         .allowAttributes("class").globally()
//         .allowStandardUrlProtocols()
//         .toFactory();

//     @Override
//     public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//         String value = p.getValueAsString();
//         if (value == null) {
//             return null;
//         }

//         // Skip processing if no HTML tags detected (preserves emails/plain text)
//         if (!HTML_TAG_PATTERN.matcher(value).find()) {
//             return value;
//         }

//         String decoded = StringEscapeUtils.unescapeHtml4(value);
        
//         // Strict XSS detection
//         if (containsXss(decoded)) {
//             throw new IOException("XSS attack detected. Input rejected.");
//         }

//         // Safe HTML sanitization
//         String sanitized = HTML_SANITIZER.sanitize(decoded);
        
//         // Verify no malicious content remains
//         if (containsXss(sanitized)) {
//             throw new IOException("Post-sanitization XSS detection. Input rejected.");
//         }
        
//         return sanitized;
//     }

//     private boolean containsXss(String input) {
//         return XSS_PATTERN.matcher(input).find() ||
//                input.matches(".*<[^>]+\\son\\w+\\s*=.*") ||
//                input.contains("eval(") ||
//                input.contains("document.cookie") ||
//                input.contains("window.location") ||
//                input.contains("document.write");
//     }
// }