package cn.mrdear.intellij.related;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocCommentOwner;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;
import com.intellij.util.PlatformIcons;

import org.jetbrains.annotations.NotNull;

import cn.mrdear.intellij.related.util.FileUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 定位注释中的 @doc ,定位到指定文件
 *
 * @author Quding Ding
 * @since 2020/2/23
 */
public class RelatedFileLineMarkerProvider extends RelatedItemLineMarkerProvider {

    private static AtomicInteger COUNT = new AtomicInteger();

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo> result) {
        PsiElement parent = element.getParent();
        if (element instanceof PsiIdentifier && (parent instanceof PsiMethod || parent instanceof PsiClass)) {
            // 定位到注释
            PsiDocComment docComment = ((PsiDocCommentOwner) parent).getDocComment();
            if (null == docComment) {
                return;
            }
            System.out.println(COUNT.getAndIncrement());
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

                List<PsiFile> files = FileUtils.queryFile(element.getProject(), file);
                if (files.isEmpty()) {
                    continue;
                }
                NavigationGutterIconBuilder<PsiElement> builder =
                    NavigationGutterIconBuilder.create(PlatformIcons.CUSTOM_FILE_ICON)
                        .setTargets(files)
                        .setTooltipText("Navigate to this files");
                result.add(builder.createLineMarkerInfo(doc.getFirstChild()));
            }
        }


    }

}
