package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import model.Schedule;
import output.OutputIpe;
import output.OutputIpeOptions;

/**
 * This frame presents the user with a number of options to customize
 * the appearance of a schedule in Ipe. It is modal, which is why
 * a reference to the parent frame is needed.
 * 
 * <p>When the user has selected options (or has aborted), the frame will
 * close itself and reenable the parent frame. 
 * 
 * @author Thom Castermans
 */
public class IpeOutputOptionFrame extends JFrame {

	/** Serial version UID. */
	private static final long serialVersionUID = -1054783210470329986L;
	
	/** OutputIpe object used to generate result. */
	private final OutputIpe oi;
	/** Schedule that should be outputted. */
	private final Schedule s;
	/** Options for output. */
	private final OutputIpeOptions oio;
	
	/** Checkbox indicating "useColors" option value. */
	private JCheckBox optionUseColors;
	/** Checkbox indicating "fill" option value. */
	private JCheckBox optionFill;
	
	/** RadioButton in group of axisnumbering option:
	 *  indicates the user wants no numbers whatsoever along X-axis.
	 */
	private JRadioButton axisNumberNoNumbers;
	/** RadioButton in group of axisnumbering option:
	 *  indicates the user wants all numbers along X-axis.
	 */
	private JRadioButton axisNumberAllNumbers;
	/** RadioButton in group of axisnumbering option:
	 *  indicates the user wants an exact number of numbers along X-axis.
	 */
	private JRadioButton axisNumberExactNumbers;
	
	/** TextField where user can type number of numbers s/he wants along X-axis. */
	private JTextField axisNumberExactNumberInput;
	/** Listener making sure only one radiobutton at a time is selected.
	 *  Will also update value in OIO.
	 */
	private ActionListener axisNumberListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() != axisNumberNoNumbers)  axisNumberNoNumbers.setSelected(false);
			if (e.getSource() != axisNumberAllNumbers)  axisNumberAllNumbers.setSelected(false);
			if (e.getSource() != axisNumberExactNumbers)  axisNumberExactNumbers.setSelected(false);
			if (e.getSource() == axisNumberNoNumbers)  oio.setOption("xAxisNumbering", 0);
			if (e.getSource() == axisNumberAllNumbers)  oio.setOption("xAxisNumbering", 1);
		}
	};
	/** Input with X-axis prelabel text. */
	private JTextField xAxisPreLabelInput;
	/** Input with X-axis postlabel text. */
	private JTextField xAxisPostLabelInput;
	/** Input with Y-axis prelabel text. */
	private JTextField yAxisPreLabelInput;
	/** Input with Y-axis postlabel text. */
	private JTextField yAxisPostLabelInput;
	
	/** Input with schedule maximum length. */
	private JTextField scheduleMaxLengthInput;
	/** TODO */
	private JRadioButton scheduleMaxLengthLcmInput;
	/** TODO */
	private JRadioButton scheduleMaxLengthCustomInput;
	/** TODO */
	private ActionListener scheduleMaxLengthListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() != scheduleMaxLengthLcmInput)  scheduleMaxLengthLcmInput.setSelected(false);
			if (e.getSource() != scheduleMaxLengthCustomInput)  scheduleMaxLengthCustomInput.setSelected(false);
			if (e.getSource() == scheduleMaxLengthLcmInput)  oio.setOption("scheduleMaxLength", 0);
		}
	};
	
	/**
	 * Create a new {@link IpeOutputOptionFrame} with given parent.
	 * 
	 * @param parentFrame The parent of this frame.
	 * @param outputIpe OutputIpe object, to be able to output the result.
	 * @param schedule The schedule to be output.
	 */
	public IpeOutputOptionFrame(final JFrame parentFrame, OutputIpe outputIpe, Schedule schedule) {
		this.oi = outputIpe;
		this.s = schedule;
		this.oio = new OutputIpeOptions();
		
		// Some initialization
		setTitle("ScheduleToIpe - Choose options for output");
		setSize(new Dimension(600, 400));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) { /* ignored */ }
			
			@Override
			public void windowIconified(WindowEvent e) { /* ignored */ }
			
			@Override
			public void windowDeiconified(WindowEvent e) { /* ignored */ }
			
			@Override
			public void windowDeactivated(WindowEvent e) { /* ignored */ }
			
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				parentFrame.setEnabled(true);
			}
			
			@Override
			public void windowActivated(WindowEvent e) { /* ignored */ }
		});
		setLayout(new BorderLayout());
		
		// OPTIONS
		JPanel formOuterPanel = new JPanel(new BorderLayout());
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.PAGE_AXIS));
		
		// General options
		JPanel generalOptionsPanel = new JPanel(new GridLayout(2, 1));
		generalOptionsPanel.setBorder(BorderFactory.createTitledBorder("General output options"));
		optionUseColors = new JCheckBox("Use colors", oio.getBooleanOption("useColors"));
		optionUseColors.setToolTipText("Indicate if you want all tasks to have a different color, or use black for all tasks.");
		generalOptionsPanel.add(optionUseColors);
		optionFill = new JCheckBox("Fill boxes in schedule", oio.getBooleanOption("fill"));
		optionFill.setToolTipText("Indicate if you want the boxes representing running tasks to be filled or not.");
		generalOptionsPanel.add(optionFill);
		formPanel.add(generalOptionsPanel);
		
		// Axis options
		JPanel axisOptionsPanel = new JPanel();
		axisOptionsPanel.setLayout(new BoxLayout(axisOptionsPanel, BoxLayout.PAGE_AXIS));
		axisOptionsPanel.setBorder(BorderFactory.createTitledBorder("Axis options"));
		JPanel axisNumberingOptionPanel = new JPanel(new GridLayout(4, 2));
		axisNumberingOptionPanel.add(new JLabel("Numbering along X-axis (time)"));
		axisNumberNoNumbers = new JRadioButton("No numbering", oio.getIntegerOption("xAxisNumbering") == 0);
		axisNumberNoNumbers.addActionListener(axisNumberListener);
		axisNumberingOptionPanel.add(axisNumberNoNumbers);
		axisNumberingOptionPanel.add(new JPanel());
		axisNumberAllNumbers = new JRadioButton("All numbers", oio.getIntegerOption("xAxisNumbering") < 0);
		axisNumberAllNumbers.addActionListener(axisNumberListener);
		axisNumberingOptionPanel.add(axisNumberAllNumbers);
		axisNumberingOptionPanel.add(new JPanel());
		axisNumberExactNumbers = new JRadioButton("Exactly so many numbers...", oio.getIntegerOption("xAxisNumbering") > 0);
		axisNumberExactNumbers.addActionListener(axisNumberListener);
		axisNumberingOptionPanel.add(axisNumberExactNumbers);
		axisNumberingOptionPanel.add(new JPanel());
		axisNumberExactNumberInput = new JTextField(
				oio.getIntegerOption("xAxisNumbering") > 0
					? String.valueOf(oio.getIntegerOption("xAxisNumbering"))
					: "2"
			);
		axisNumberingOptionPanel.add(axisNumberExactNumberInput);
		axisOptionsPanel.add(axisNumberingOptionPanel);
		
		JPanel xAxisPreLabelPanel = new JPanel(new GridLayout(1, 2));
		xAxisPreLabelPanel.add(new JLabel("X-axis pre-label text:"));
		xAxisPreLabelInput = new JTextField(oio.getStringOption("xAxisPreLabelText"));
		xAxisPreLabelPanel.add(xAxisPreLabelInput);
		axisOptionsPanel.add(xAxisPreLabelPanel);
		JPanel xAxisPostLabelPanel = new JPanel(new GridLayout(1, 2));
		xAxisPostLabelPanel.add(new JLabel("X-axis post-label text:"));
		xAxisPostLabelInput = new JTextField(oio.getStringOption("xAxisPostLabelText"));
		xAxisPostLabelPanel.add(xAxisPostLabelInput);
		axisOptionsPanel.add(xAxisPostLabelPanel);
		JPanel yAxisPreLabelPanel = new JPanel(new GridLayout(1, 2));
		yAxisPreLabelPanel.add(new JLabel("Y-axis pre-label text:"));
		yAxisPreLabelInput = new JTextField(oio.getStringOption("yAxisPreLabelText"));
		yAxisPreLabelPanel.add(yAxisPreLabelInput);
		axisOptionsPanel.add(yAxisPreLabelPanel);
		JPanel yAxisPostLabelPanel = new JPanel(new GridLayout(1, 2));
		yAxisPostLabelPanel.add(new JLabel("Y-axis post-label text:"));
		yAxisPostLabelInput = new JTextField(oio.getStringOption("yAxisPostLabelText"));
		yAxisPostLabelPanel.add(yAxisPostLabelInput);
		axisOptionsPanel.add(yAxisPostLabelPanel);
		formPanel.add(axisOptionsPanel);
		
		// Miscellaneous options
		JPanel miscOptionsPanel = new JPanel();
		miscOptionsPanel.setLayout(new BoxLayout(miscOptionsPanel, BoxLayout.PAGE_AXIS));
		miscOptionsPanel.setBorder(BorderFactory.createTitledBorder("Miscellaneous options"));
		JPanel scheduleMaxLengthPanel = new JPanel(new GridLayout(3, 2));
		scheduleMaxLengthPanel.add(new JLabel("Maximum length of the schedule"));
		scheduleMaxLengthLcmInput = new JRadioButton("LCM", oio.getIntegerOption("scheduleMaxLength") == 0);
		scheduleMaxLengthLcmInput.addActionListener(scheduleMaxLengthListener);
		scheduleMaxLengthPanel.add(scheduleMaxLengthLcmInput);
		scheduleMaxLengthPanel.add(new JPanel());
		scheduleMaxLengthCustomInput = new JRadioButton("Custom value...", oio.getIntegerOption("scheduleMaxLength") != 0);
		scheduleMaxLengthCustomInput.addActionListener(scheduleMaxLengthListener);
		scheduleMaxLengthPanel.add(scheduleMaxLengthCustomInput);
		scheduleMaxLengthPanel.add(new JPanel());
		scheduleMaxLengthInput = new JTextField(
				oio.getIntegerOption("scheduleMaxLength") == 0
					? String.valueOf(schedule.getLcm())
					: String.valueOf(oio.getIntegerOption("scheduleMaxLength"))
			);
		scheduleMaxLengthPanel.add(scheduleMaxLengthInput);
		miscOptionsPanel.add(scheduleMaxLengthPanel);
		formPanel.add(miscOptionsPanel);
		
		formOuterPanel.add(formPanel, BorderLayout.NORTH);
		add(new JScrollPane(formOuterPanel), BorderLayout.CENTER);
		
		// OK/CANCEL
		JPanel okCancelPanel = new JPanel(new BorderLayout());
		JPanel okCancelInnerPanel = new JPanel();
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		okCancelInnerPanel.add(cancelButton);
		JButton okButton = new JButton("Generate schedule");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				oio.setOption("useColors", optionUseColors.isSelected());
				oio.setOption("fill", optionFill.isSelected());
				if (axisNumberExactNumbers.isSelected()) {
					oio.setOption("xAxisNumbering", Integer.parseInt(axisNumberExactNumberInput.getText()));
				}
				oio.setOption("xAxisPreLabelText", xAxisPreLabelInput.getText());
				oio.setOption("xAxisPostLabelText", xAxisPostLabelInput.getText());
				oio.setOption("yAxisPreLabelText", yAxisPreLabelInput.getText());
				oio.setOption("yAxisPostLabelText", yAxisPostLabelInput.getText());
				if (scheduleMaxLengthCustomInput.isSelected()) {
					oio.setOption("scheduleMaxLength", Integer.parseInt(scheduleMaxLengthInput.getText()));
				}
				//System.out.println(oio);
				//System.out.println(schedule);
				oi.outputIpeFile(s, oio);
				dispose();
			}
		});
		okCancelInnerPanel.add(okButton);
		okCancelPanel.add(okCancelInnerPanel, BorderLayout.EAST);
		add(okCancelPanel, BorderLayout.SOUTH);
		
		// Disable parentframe
		parentFrame.setEnabled(false);
	}
}
