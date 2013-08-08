package gui.popups.menu;

import gui.menu.MenuFiller;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import main.Launcher;
import network.Networker;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.w3c.dom.Document;
import utilities.GenericPrompt;
import utilities.XMLParser;

import applications.App;

public class ChooseConfigsPrompt {
	public static final void launch(final File[] files, Shell mainShell) {
		final Shell shell = GenericPrompt.getGenericShell();
		final Tree tree = new Tree(shell, SWT.BORDER | SWT.CHECK);
		final Button okayB = new Button(shell, SWT.NONE);

		developShell(shell, tree, okayB);
		Document[] docsold = getDocuments(tree, files);
		final Document[] docs = populateTree(docsold, tree);
		addOkayButtonListener(tree, docs, okayB, shell, mainShell);
		addTreeListener(tree, okayB);

		TreeItem[] items = tree.getItems();
		for (TreeItem item : items) {
			item.setChecked(true);
			boolean checked = item.getChecked();
			checkItems(item, checked);
			checkPath(item.getParentItem(), checked, false);
			okayB.setFocus();
		}

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!Display.getCurrent().readAndDispatch())
				Display.getCurrent().sleep();
		}
	}

	private static final void developShell(final Shell shell, Tree tree,
			Button okayB) {
		shell.setText("Choose App Configurations");

		FormLayout formLayout = new FormLayout();
		formLayout.marginBottom = 6;
		formLayout.marginTop = 6;
		formLayout.marginLeft = 6;
		formLayout.marginRight = 6;

		shell.setLayout(formLayout);
		shell.addShellListener(new ShellAdapter() {
			@Override
			public final void shellClosed(ShellEvent e) {
				e.doit = false;
				shell.setVisible(false);
			}
		});

		FormData data = new FormData();
		data.width = 200;
		data.height = 200;
		tree.setLayoutData(data);

		okayB.setText("Open");
		data = new FormData();
		data.top = new FormAttachment(tree, 10);
		data.right = new FormAttachment(100, 0);
		data.width = 50;
		okayB.setLayoutData(data);
		okayB.setFocus();
	}

	private static final Document[] populateTree(Document[] docs, Tree tree) {
		ArrayList<Document> docsArray = new ArrayList<Document>();

		for (Document d : docs) {
			try {
				addAppBranch(tree, d);
				docsArray.add(d);
			} catch (NullPointerException e) {
			}
		}

		return docsArray.toArray(new Document[docsArray.size()]);
	}

	private static final Document[] getDocuments(Tree tree, final File[] files) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		ArrayList<Document> documents = new ArrayList<Document>();
		try {
			final DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();

			for (int i = 0; i < files.length; i++) {
				try {
					Document doc = documentBuilder.parse(files[i]);
					doc.getDocumentElement().normalize();
					documents.add(doc);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return documents.toArray(new Document[documents.size()]);
	}

	private static final void addOkayButtonListener(final Tree tree,
			final Document[] docs, Button b, final Shell s,
			final Shell mainShell) {
		b.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (!NavConstantXMLPrompt.launch())
					return;

				final ArrayList<App> apps = new ArrayList<App>();
				TreeItem[] items = tree.getItems();

				if (items.length != docs.length)
					Launcher.addUserActivity("INVALID XML LOADED");
				else {
					for (int i = 0; i < items.length; i++) {
						TreeItem appItem = items[i];

						for (int j = 0; j < appItem.getItemCount(); j++) {
							if (appItem.getItem(j).getChecked()) {
								String config = appItem.getItem(j).getText();
								try {
									int[] IDs = XMLParser.getIDs(docs[i], j);
									apps.add(XMLParser.getApp(docs[i], IDs[0],
											IDs[1], config));
								} catch (Throwable ex) {
									ex.printStackTrace();
								}
							}
						}
						s.close();
					}
				}
				if (apps.size() > 0) {
					Networker.monitorTelemetry();
					MenuFiller.addMenu(mainShell,
							apps.toArray(new App[apps.size()]), "1.0.0");
				}
			}
		});
	}

	private static final void checkPath(TreeItem item, boolean checked,
			boolean grayed) {
		if (item == null)
			return;
		if (grayed) {
			checked = true;
		} else {
			int index = 0;
			TreeItem[] items = item.getItems();
			while (index < items.length) {
				TreeItem child = items[index];
				if (child.getGrayed() || checked != child.getChecked()) {
					checked = grayed = true;
					break;
				}
				index++;
			}
		}
		item.setChecked(checked);
		item.setGrayed(grayed);
		checkPath(item.getParentItem(), checked, grayed);
	}

	private static final void checkItems(TreeItem item, boolean checked) {
		item.setGrayed(false);
		item.setChecked(checked);
		TreeItem[] items = item.getItems();
		for (int i = 0; i < items.length; i++) {
			checkItems(items[i], checked);
		}
	}

	public static void addTreeListener(Tree tree, final Button okayB) {
		tree.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.detail == SWT.CHECK) {
					TreeItem item = (TreeItem) event.item;
					boolean checked = item.getChecked();
					checkItems(item, checked);
					checkPath(item.getParentItem(), checked, false);
					okayB.setFocus();
				}
			}
		});
	}

	public static void addAppBranch(Tree tree, Document document)
			throws NullPointerException {
		ArrayList<String> configNames = XMLParser.getConfigNames(document);

		String name = XMLParser.getName(document);

		TreeItem appItem = new TreeItem(tree, SWT.NONE);
		appItem.setText(name);
		for (int i = 0; i < configNames.size(); i++) {
			TreeItem sub = new TreeItem(appItem, SWT.NONE);
			sub.setText(configNames.get(i));
		}
	}
}