package cn.mrdear.intellij.related;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;

import cn.mrdear.intellij.related.util.FileUtils;

import java.util.Collection;

/**
 * @author Quding Ding
 * @since 2020/2/24
 */
public class FileSearchLazyValue extends NotNullLazyValue<Collection<? extends PsiElement>> {

    private Project project;

    private String fileName;

    public FileSearchLazyValue(Project project, String fileName) {
        this.project = project;
        this.fileName = fileName;
    }

    @NotNull
    @Override
    protected Collection<? extends PsiElement> compute() {
        return FileUtils.queryFile(project, fileName);
    }

}
