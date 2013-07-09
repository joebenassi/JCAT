package gui.menu;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

final class MenuHelper {
	static final Menu createMenu(final Shell parent, final int style) {
		Menu m = new Menu(parent, style);
		return m;
	}

	static final Menu createMenu(final Shell parent, final int style,
			final MenuItem container, final boolean enabled) {
		Menu m = createMenu(parent, style);
		m.setEnabled(enabled);
		container.setMenu(m);
		return m;
	}

	static final MenuItem createMenuItem(final Menu parent, final int style,
			final String text, final Image icon, final int accel) {
		MenuItem mi = new MenuItem(parent, style);
		if (text != null)
			mi.setText(text);
		if (icon != null)
			mi.setImage(icon);
		if (accel != -1)
			mi.setAccelerator(accel);

		return mi;
	}
}
