package cn.mrdear.intellij.related;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocCommentOwner;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;
import com.intellij.util.PlatformIcons;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * 定位注释中的 @doc ,定位到指定文件
 * @doc example.gif
 * @author Quding Ding
 * @since 2020/2/23
 */
public class RelatedFileLineMarkerProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo> result) {
        PsiElement parent = element.getParent();
        if (element instanceof PsiIdentifier && (parent instanceof PsiMethod || parent instanceof PsiClass)) {
            // 定位到注释
            PsiDocComment docComment = ((PsiDocCommentOwner) parent).getDocComment();
            if (null == docComment) {
                return;
            }
            // 定位到tags
            PsiDocTag[] docs = docComment.findTagsByName("doc");
            if (docs.length == 0) {
                return;
            }
            for (PsiDocTag doc : docs) {
                // 找到值
                PsiDocTagValue value = doc.getValueElement();
                if (null == value) {
                    continue;
                }
                String file = value.getText();
                if (!file.contains(".")) {
                    continue;
                }

                NavigationGutterIconBuilder<PsiElement> builder =
                    NavigationGutterIconBuilder.create(PlatformIcons.CUSTOM_FILE_ICON)
                        .setTargets(new FileSearchLazyValue(element.getProject(), file))
                        .setEmptyPopupText("This file not found")
                        .setTooltipText("Navigate to this files");

                result.add(builder.createLineMarkerInfo(doc.getFirstChild()));
            }
        }


    }

}
