package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
				oi.outputIpeFile(s, oio);
				//System.out.println(schedule);
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
