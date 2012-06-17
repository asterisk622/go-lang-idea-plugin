package ro.redeul.google.go.intentions.conversions;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import ro.redeul.google.go.intentions.Intention;
import ro.redeul.google.go.lang.psi.expressions.primary.GoLiteralExpression;

import static ro.redeul.google.go.intentions.conversions.ConversionUtil.isHexInteger;
import static ro.redeul.google.go.intentions.conversions.ConversionUtil.isOctalInteger;

/**
 * Convert hexadecimal or octal integer to decimal
 */
public class ConvertIntegerToDecimalIntention extends Intention {
    @Override
    protected boolean satisfiedBy(PsiElement element) {
        if (!(element instanceof GoLiteralExpression)) {
            return false;
        }

        String text = element.getText();
        return isHexInteger(text) || isOctalInteger(text);
    }

    @Override
    protected void processIntention(@NotNull PsiElement element, Project project, Editor editor)
            throws IncorrectOperationException {
        int value;
        try {
            String text = element.getText();
            if (isHexInteger(text)) {
                value = Integer.parseInt(text.substring(2), 16);
            } else {
                value = Integer.parseInt(text, 8);
            }
        } catch (NumberFormatException e) {
            throw new IncorrectOperationException("Invalid integer");
        }

        String result = Integer.toString(value);
        int start = element.getTextOffset();
        int end = start + element.getTextLength();
        editor.getDocument().replaceString(start, end, result);
    }
}
