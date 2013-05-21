/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.markiewb.netbeans.plugin.git.showbranch;

import static de.markiewb.netbeans.plugin.git.showbranch.GitUtils.getGitRepoDirectory;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingWorker;
import org.netbeans.libs.git.GitBranch;
import org.netbeans.libs.git.GitClient;
import org.netbeans.libs.git.GitException;
import org.netbeans.libs.git.GitRepository;
import org.netbeans.libs.git.progress.ProgressMonitor;
import org.openide.awt.JPopupMenuUtils;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public class Installer extends ModuleInstall implements PropertyChangeListener {

    RequestProcessor requestProcessor = new RequestProcessor(Installer.class);

    @Override
    public void restored() {
        final PropertyChangeListener listenerA = this;
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                // code to be invoked when system UI is ready
                TopComponent.getRegistry().addPropertyChangeListener(listenerA);
            }
        });
    }

    @Override
    public void uninstalled() {
        TopComponent.getRegistry().removePropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //run inside EDT, because we are using WindowManager inside
        final FileObject path = new PathUtils().getPath();
        final PropertyChangeListener listener=this;
        //run the lenghty GIT processing outside EDT
        requestProcessor.execute(new Runnable() {
            @Override
            public void run() {
                final FileObject gitRepoDirectory = getGitRepoDirectory(path);
                if (null == gitRepoDirectory) {
                    return;
                }

                final GitBranch activeBranch = GitUtils.getActiveBranch(path);
                if (null != activeBranch) {
                    BranchStatusLineElement.jLabel.setText(activeBranch.getName());
                    BranchStatusLineElement.jLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            JPopupMenu menu = new JPopupMenu("Git Branch Popup");
                            Collection<GitBranch> branches = GitUtils.getBranches(path, true);
                            JMenuItem groupCurrent = new JMenuItem("____Current Branch____");
                            groupCurrent.setEnabled(false);
                            menu.add(groupCurrent);
                            for (GitBranch branch : branches) {
                                if (!branch.isRemote() && branch.isActive()) {
                                    if (branch.isActive()) {
                                        String to = null != branch.getTrackedBranch() ? " -> " + branch.getTrackedBranch().getName() : "";
                                        menu.add(new JMenuItem(branch.getName() + to));
                                        break;
                                    }
                                }
                            }
                            //Checkout, Checkout as new branch, Compare, Merge, Delete
                            JMenuItem groupLocal = new JMenuItem("____Local Branches____");
                            groupLocal.setEnabled(false);
                            menu.add(groupLocal);
                            for (final GitBranch branch : branches) {
                                if (!branch.isRemote() && !branch.isActive()) {
                                    String to = null != branch.getTrackedBranch() ? " -> " + branch.getTrackedBranch().getName() : "";
                                    final JMenu subMenu = new JMenu(branch.getName() + to);
                                    menu.add(subMenu);
                                    subMenu.add(new JMenuItem(new CheckoutBranchAction(branch)));
                                }
                            }

                            JMenuItem groupRemote = new JMenuItem("____Remote Branches____");
                            groupRemote.setEnabled(false);
                            menu.add(groupRemote);
                            for (GitBranch branch : branches) {
                                if (branch.isRemote()) {
//                                    String to = null != branch.getTrackedBranch() ? " -> " + branch.getTrackedBranch().getName() : "";
                                    String to = "";
                                    final JMenuItem item = new JMenuItem(branch.getName() + to);
                                    menu.add(item);
                                }
                            }

                            menu.show(BranchStatusLineElement.jLabel, 0, 0);
                        }

                        class CheckoutBranchAction extends AbstractAction {

                            private final GitBranch branch;

                            public CheckoutBranchAction(GitBranch branch) {
                                this.branch = branch;
//                                putValue(NAME, "Checkout "+branch.getId() + " "+path.getPath());
                                putValue(NAME, "Checkout");
                            }

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                GitRepository repo = GitRepository.getInstance(FileUtil.toFile(gitRepoDirectory));
                                GitClient client = null;
                                try {
                                    client = repo.createClient();
                                    final InputOutput io = IOProvider.getDefault().getIO("Git Branch Popup", false);
                                    ProgressMonitor progressMonitor = new DefaultProgressMonitorImpl(io, listener);
                                    String revision = branch.getName();
                                    client.reset("HEAD", GitClient.ResetType.HARD, progressMonitor);
                                    client.checkoutRevision(revision, true, progressMonitor);
                                } catch (GitException ex) {
                                    Exceptions.printStackTrace(ex);
                                } finally {
                                    if (null != client) {
                                        client.release();
                                    }
                                }
                            }

                            class DefaultProgressMonitorImpl extends ProgressMonitor.DefaultProgressMonitor {

                                private final InputOutput io;
                                private final PropertyChangeListener listener;

                                public DefaultProgressMonitorImpl(InputOutput io, PropertyChangeListener listener) {
                                    this.io = io;
                                    this.listener = listener;
                                }

                                @Override
                                public void notifyError(String message) {
                                    io.getOut().println("ERROR: " + message);
                                    listener.propertyChange(null);
                                }

                                @Override
                                public void notifyWarning(String message) {
                                    io.getOut().println("WARN: " + message);
                                    listener.propertyChange(null);
                                }

                                @Override
                                public void preparationsFailed(String message) {
                                    io.getOut().println("FAILED preparation = " + message);
                                    listener.propertyChange(null);
                                }

                                @Override
                                public void started(String command) {
                                    io.getOut().println(command);
                                }

                                @Override
                                public void finished() {
//                                            io.getOut().println("finished");
                                    listener.propertyChange(null);
                                }
                            }
                        }
                    });
                } else {
                    BranchStatusLineElement.jLabel.setText("");
                }
            }
        });

    }
}
