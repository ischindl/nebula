/*******************************************************************************
 * Copyright (c) 2010, 2017 Oak Ridge National Laboratory and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.eclipse.nebula.visualization.xygraph.figures;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.ButtonGroup;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToggleButton;
import org.eclipse.draw2d.ToggleModel;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.visualization.internal.xygraph.toolbar.AddAnnotationDialog;
import org.eclipse.nebula.visualization.internal.xygraph.toolbar.GrayableButton;
import org.eclipse.nebula.visualization.internal.xygraph.toolbar.RemoveAnnotationDialog;
import org.eclipse.nebula.visualization.internal.xygraph.toolbar.WrappableToolbarLayout;
import org.eclipse.nebula.visualization.internal.xygraph.toolbar.XYGraphConfigDialog;
import org.eclipse.nebula.visualization.internal.xygraph.undo.AddAnnotationCommand;
import org.eclipse.nebula.visualization.internal.xygraph.undo.IOperationsManagerListener;
import org.eclipse.nebula.visualization.internal.xygraph.undo.OperationsManager;
import org.eclipse.nebula.visualization.internal.xygraph.undo.RemoveAnnotationCommand;
import org.eclipse.nebula.visualization.xygraph.util.SingleSourceHelper2;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * The toolbar for an xy-graph.
 * 
 * @author Xihui Chen
 * @author Kay Kasemir (some zoom operations)
 */
public class XYGraphToolbar extends Figure {
	private final static int BUTTON_SIZE = 25;

	final private IXYGraph xyGraph;

	final private ButtonGroup zoomGroup;

	final private Map<ZoomType, ToggleModel> zoomButtonModelMap = new HashMap<ZoomType, ToggleModel>();

	/**
	 * Initialize
	 *
	 * @param xyGraph
	 *            XYGraph on which this toolbar operates
	 * @param flags
	 *            Bitwise 'or' of flags
	 * @see XYGraphFlags#COMBINED_ZOOM
	 * @see XYGraphFlags#SEPARATE_ZOOM
	 */
	public XYGraphToolbar(final IXYGraph xyGraph, final int flags) {
		this.xyGraph = xyGraph;
		setLayoutManager(new WrappableToolbarLayout());

		final Button configButton = new Button(XYGraphMediaFactory.getInstance().getImage("images/Configure.png"));
		configButton.setToolTip(new Label("Configure Settings..."));
		addButton(configButton);
		configButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				XYGraphConfigDialog dialog = new XYGraphConfigDialog(Display.getCurrent().getActiveShell(), xyGraph);
				dialog.open();
			}
		});

		final ToggleButton showLegend = new ToggleButton("",
				XYGraphMediaFactory.getInstance().getImage("images/ShowLegend.png"));
		showLegend.setToolTip(new Label("Show Legend"));
		addButton(showLegend);
		showLegend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				xyGraph.setShowLegend(!xyGraph.isShowLegend());
			}
		});

		showLegend.setSelected(xyGraph.isShowLegend());

		addSeparator();
		final Button addAnnotationButton = new Button(
				XYGraphMediaFactory.getInstance().getImage("images/Add_Annotation.png"));
		addAnnotationButton.setToolTip(new Label("Add Annotation..."));
		addButton(addAnnotationButton);
		addAnnotationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				AddAnnotationDialog dialog = new AddAnnotationDialog(Display.getCurrent().getActiveShell(), xyGraph);
				if (dialog.open() == Window.OK) {
					xyGraph.addAnnotation(dialog.getAnnotation());
					xyGraph.getOperationsManager()
							.addCommand(new AddAnnotationCommand(xyGraph, dialog.getAnnotation()));
				}
			}
		});

		final Button delAnnotationButton = new Button(
				XYGraphMediaFactory.getInstance().getImage("images/Del_Annotation.png"));
		delAnnotationButton.setToolTip(new Label("Remove Annotation..."));
		addButton(delAnnotationButton);
		delAnnotationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				RemoveAnnotationDialog dialog = new RemoveAnnotationDialog(Display.getCurrent().getActiveShell(),
						xyGraph);
				if (dialog.open() == Window.OK && dialog.getAnnotation() != null) {
					xyGraph.removeAnnotation(dialog.getAnnotation());
					xyGraph.getOperationsManager()
							.addCommand(new RemoveAnnotationCommand(xyGraph, dialog.getAnnotation()));
				}
			}
		});

		addSeparator();
		if ((flags & XYGraphFlags.STAGGER) > 0) { // stagger axes button
			final Button staggerButton = new Button(XYGraphMediaFactory.getInstance().getImage("images/stagger.png"));
			staggerButton.setToolTip(new Label("Stagger axes so they don't overlap"));
			addButton(staggerButton);
			staggerButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					xyGraph.performStagger();
				}
			});
		} else { // auto scale button
			final Button autoScaleButton = new Button(
					XYGraphMediaFactory.getInstance().getImage("images/AutoScale.png"));
			autoScaleButton.setToolTip(new Label("Perform Auto Scale"));
			addButton(autoScaleButton);
			autoScaleButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					xyGraph.performAutoScale();
				}
			});
		}

		// zoom buttons
		zoomGroup = new ButtonGroup();
		createZoomButtons(flags);

		addSeparator();
		addUndoRedoButtons();

		addSeparator();
		if (!SWT.getPlatform().startsWith("rap")) //$NON-NLS-1$
			addSnapshotButton();
	}

	// @Override
	// public boolean isOpaque() {
	// return true;
	// }

	private void addSnapshotButton() {
		Button snapShotButton = new Button(XYGraphMediaFactory.getInstance().getImage("images/camera.png"));
		snapShotButton.setToolTip(new Label("Save Snapshot to PNG file"));
		addButton(snapShotButton);
		snapShotButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// Have valid name, so get image
				ImageLoader loader = new ImageLoader();
				Image image = xyGraph.getImage();
				loader.data = new ImageData[] { image.getImageData() };
				image.dispose();
				// Prompt for file name
				String path = SingleSourceHelper2.getImageSavePath();
				if (path == null || path.length() <= 0)
					return;
				// Assert *.png at end of file name
				if (!path.toLowerCase().endsWith(".png"))
					path = path + ".png";
				// Save
				loader.save(path, SWT.IMAGE_PNG);
			}
		});
	}

	private void addUndoRedoButtons() {
		// undo button
		final GrayableButton undoButton = new GrayableButton(
				XYGraphMediaFactory.getInstance().getImage("images/Undo.png"), //$NON-NLS-1$
				XYGraphMediaFactory.getInstance().getImage("images/Undo_Gray.png")); //$NON-NLS-1$
		undoButton.setToolTip(new Label("Undo"));
		undoButton.setEnabled(false);
		addButton(undoButton);
		undoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				xyGraph.getOperationsManager().undo();
			}
		});
		xyGraph.getOperationsManager().addListener(new IOperationsManagerListener() {
			public void operationsHistoryChanged(OperationsManager manager) {
				if (manager.getUndoCommandsSize() > 0) {
					undoButton.setEnabled(true);
					final String cmd_name = manager.getUndoCommands()[manager.getUndoCommandsSize() - 1].toString();
					undoButton.setToolTip(new Label("Undo" + cmd_name));
				} else {
					undoButton.setEnabled(false);
					undoButton.setToolTip(new Label("Undo"));
				}
			}
		});

		// redo button
		final GrayableButton redoButton = new GrayableButton(
				XYGraphMediaFactory.getInstance().getImage("images/Redo.png"), //$NON-NLS-1$
				XYGraphMediaFactory.getInstance().getImage("images/Redo_Gray.png")); //$NON-NLS-1$
		redoButton.setToolTip(new Label("Redo"));
		redoButton.setEnabled(false);
		addButton(redoButton);
		redoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				xyGraph.getOperationsManager().redo();
			}
		});
		xyGraph.getOperationsManager().addListener(new IOperationsManagerListener() {
			public void operationsHistoryChanged(OperationsManager manager) {
				if (manager.getRedoCommandsSize() > 0) {
					redoButton.setEnabled(true);
					final String cmd_name = manager.getRedoCommands()[manager.getRedoCommandsSize() - 1].toString();
					redoButton.setToolTip(new Label("Redo" + cmd_name));
				} else {
					redoButton.setEnabled(false);
					redoButton.setToolTip(new Label("Redo"));
				}
			}
		});
	}

	/**
	 * Create buttons enumerated in <code>ZoomType</code>
	 * 
	 * @param flags
	 *            Bitwise 'or' of flags
	 * @see XYGraphFlags#COMBINED_ZOOM
	 * @see XYGraphFlags#SEPARATE_ZOOM
	 */
	private void createZoomButtons(final int flags) {
		for (final ZoomType zoomType : ZoomType.values()) {
			if (!zoomType.useWithFlags(flags))
				continue;
			final ImageFigure imageFigure = new ImageFigure(zoomType.getIconImage());
			final Label tip = new Label(zoomType.getDescription());
			final ToggleButton button = new ToggleButton(imageFigure);
			button.setBackgroundColor(ColorConstants.button);
			button.setOpaque(true);
			final ToggleModel model = new ToggleModel();
			model.addChangeListener(new ChangeListener() {
				public void handleStateChanged(ChangeEvent event) {
					if (event.getPropertyName().equals("selected") && button.isSelected()) {
						xyGraph.setZoomType(zoomType);
					}
				}
			});

			button.setModel(model);
			zoomButtonModelMap.put(zoomType, model);
			button.setToolTip(tip);
			addButton(button);
			zoomGroup.add(model);

			if (zoomType == ZoomType.NONE)
				zoomGroup.setDefault(model);
		}
		xyGraph.addPropertyChangeListener(IXYGraph.PROPERTY_ZOOMTYPE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				zoomGroup.setSelected(zoomButtonModelMap.get(evt.getNewValue()));
			}
		});
	}

	public void addButton(Clickable button) {
		button.setPreferredSize(BUTTON_SIZE, BUTTON_SIZE);
		add(button);
	}

	public void addSeparator() {
		ToolbarSeparator separator = new ToolbarSeparator();
		separator.setPreferredSize(BUTTON_SIZE / 2, BUTTON_SIZE);
		add(separator);
	}

	private static class ToolbarSeparator extends Figure {

		private final Color GRAY_COLOR = XYGraphMediaFactory.getInstance().getColor(new RGB(130, 130, 130));

		@Override
		protected void paintClientArea(Graphics graphics) {
			super.paintClientArea(graphics);
			graphics.setForegroundColor(GRAY_COLOR);
			graphics.setLineWidth(1);
			graphics.drawLine(bounds.x + bounds.width / 2, bounds.y, bounds.x + bounds.width / 2,
					bounds.y + bounds.height);
		}
	}
}
