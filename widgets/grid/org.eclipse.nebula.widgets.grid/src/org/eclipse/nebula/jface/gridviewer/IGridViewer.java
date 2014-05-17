package org.eclipse.nebula.jface.gridviewer;

import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ViewerRow;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.widgets.Widget;

/**
 * @author schindler
 *
 */
public interface IGridViewer {
	/**
	 * @param autoPreferredHeight
	 */
	void setAutoPreferredHeight(boolean autoPreferredHeight);

	/**
	 * @return Grid
	 */
	Grid getGrid();

	/**
	 * @param b
	 */
	void setUseHashlookup(boolean b);

	/**
	 * @return boolean
	 */
	boolean getAutoPreferredHeight();

	/**
	 * @param gridViewerEditor
	 */
	void setColumnViewerEditor(ColumnViewerEditor gridViewerEditor);

	/**
	 * @param item
	 * @return @Link ViewerRow
	 */
	ViewerRow getViewerRowFromItem(Widget item);

}
