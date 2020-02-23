package cn.mrdear.intellij.related.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Quding Ding
 * @since 2020/2/23
 */
public class FileUtils {

    public static List<PsiElement> queryFile(Project project, String fileName) {
        Collection<VirtualFile> files = FilenameIndex.getVirtualFilesByName(project, fileName,
            GlobalSearchScope.allScope(project));
        if (files.isEmpty()) {
            return Collections.emptyList();
        }

        PsiManager psiManager = PsiManager.getInstance(project);
        return files.parallelStream()
            .map(psiManager::findFile)
            .filter(Objects::nonNull)
            .map(PsiElement::getNavigationElement)
            .collect(Collectors.toList());
    }

}
