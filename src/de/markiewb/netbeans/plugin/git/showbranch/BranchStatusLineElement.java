/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.markiewb.netbeans.plugin.git.showbranch;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import org.openide.awt.StatusLineElementProvider;
import org.openide.filesystems.FileUtil;
import org.openide.util.Utilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author markiewb
 */
@ServiceProvider(service = StatusLineElementProvider.class)
public final class BranchStatusLineElement implements StatusLineElementProvider {

    static JComponent comp;
    static JLabel jLabel;

    @Override
    public Component getStatusLineElement() {
        if (null == comp) {
            comp = new JPanel(new FlowLayout(FlowLayout.LEFT));
            comp.setBorder(null);
            comp.add(new JSeparator());
            jLabel = new JLabel("");
            jLabel.setBorder(null);
            comp.add(jLabel);
            final MouseAdapter mouseAdapter = new MouseAdapter() {

                          @Override
                          public void mousePressed(MouseEvent e) {
//                              List<Action> actions = new ArrayList<Action>(Utilities.actionsForPath("Actions/Git/"));
//                              actions.add(0, action);
//                              actions.add(1, null);
//                              Action[] actions1 = actions.toArray(new Action[actions.size()]);
//                              
//                              JPopupMenu actionsToPopup = Utilities.actionsToPopup(actions1, Utilities.actionsGlobalContext());
//                              actionsToPopup.show(jLabel, 0, 0);
//                              System.out.println("actions = " + actions);
                              
                              /**
                              Actions/Git/org-netbeans-modules-git-ui-actions-AddAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-blame-AnnotateAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-branch-CreateBranchAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-branch-DeleteBranchAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-checkout-CheckoutPathsAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-checkout-CheckoutRevisionAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-checkout-RevertChangesAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-checkout-SwitchBranchAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-clone-CloneAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-commit-CommitAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-commit-ExcludeFromCommitAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-commit-IncludeInCommitAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-conflicts-ResolveConflictsAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-diff-DiffAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-diff-ExportCommitAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-diff-ExportUncommittedChangesAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-fetch-FetchAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-fetch-FetchFromUpstreamAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-fetch-PullAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-fetch-PullFromUpstreamAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-history-SearchHistoryAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-ignore-IgnoreAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-ignore-UnignoreAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-init-InitAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-merge-MergeRevisionAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-output-OpenOutputAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-push-PushAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-push-PushToUpstreamAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-repository-RepositoryBrowserAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-reset-ResetAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-revert-RevertCommitAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-status-StatusAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-tag-CreateTagAction.instance
                              Actions/Git/org-netbeans-modules-git-ui-tag-ManageTagsAction.instance
                               */


                              
                          }
                      };
//            jLabel.addMouseListener(mouseAdapter);
        }

        return comp;
    }

}
