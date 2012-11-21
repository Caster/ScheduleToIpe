package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Task;

/**
 * ScheduleToIpe is a GUI program that allows a user to create
 * a taskset and then have it scheduled by different algorithms.
 * The output is an IPE file.
 * 
 * @author Thom Castermans
 */
public class ScheduleToIpe extends JFrame {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -4434864814684611817L;
	/** Text to display in list when no tasks are available yet. */
	private static final String NO_TASKS_TEXT = "No tasks created yet.";
	/** Text to display in info pane when no tasks are available yet. */
	private static final String NO_TASKS_INFO_TEXT = "Here, information about created tasks will appear. " +
			"You should first create a task to see something useful here.";
	
	/** Pane with information about selected task. */
	private JTextPane taskInfoPane;
	/** List with created tasks. */
	private JList<String> taskList;
	/** Model for list with created tasks. */
	private DefaultListModel<String> taskListModel;
	/** Listener for list with created tasks. */
	private ListSelectionListener taskListSelectionListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (createdTasks.size() > 0 && taskListModel.size() > 0 && taskList.getSelectedIndex() >= 0) {
				taskInfoPane.setText(taskListModel.getElementAt(taskList.getSelectedIndex()));
				taskInfoPane.repaint();
				removeTaskButton.setEnabled(true);
			} else {
				taskInfoPane.setText(NO_TASKS_INFO_TEXT);
			}
		}
	};
	/** "Add task" button. */
	JButton addTaskButton;
	/** Listener for "Add task" button. */
	ActionListener addTaskButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Ask for task name
			String name = JOptionPane.showInputDialog(ScheduleToIpe.this, "Enter a unique name for the task to be created.");
			// Check name
			if (name.length() == 0) {
				JOptionPane.showMessageDialog(ScheduleToIpe.this, "A task should have a name of at least one character.",
						"ScheduleToIpe - Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			// Check if we do not have a task with that name already
			for (Task t : createdTasks) {
				if (t.getName().equals(name)) {
					JOptionPane.showMessageDialog(ScheduleToIpe.this, "A task with the name \"" + name + "\" already exists.",
							"ScheduleToIpe - Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			// Remove "NO_TASKS_TEXT" from list
			if (createdTasks.size() == 0)  taskListModel.removeAllElements();
			// Add task
			taskListModel.addElement(name);
			createdTasks.add(new Task(name, 5, 5, 2));
			// Update list
			taskList.repaint();
		}
	};
	/** "Remove task" button. */
	JButton removeTaskButton;
	/** Listener for "Remove task" button. */
	ActionListener removeTaskButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Check if there is actually a task selected... should be the case :)
			int prevSelected = taskList.getSelectedIndex();
			if (prevSelected == -1)  return;
			// Remove tasks from internal set
			for (Task t : createdTasks) {
				if (t.getName().equals(taskListModel.getElementAt(taskList.getSelectedIndex()))) {
					createdTasks.remove(t);
					break;
				}
			}
			// Remove task from model, add special text if no tasks left
			taskListModel.remove(taskList.getSelectedIndex());
			if (taskListModel.size() == 0) {
				taskListModel.addElement(NO_TASKS_TEXT);
			}
			// Select next task in list, or disable remove task button as no task can be removed
			if (createdTasks.size() > 0) {
				if (taskListModel.size() > prevSelected) {
					taskList.setSelectedIndex(prevSelected);
				} else {
					taskList.setSelectedIndex(0);
				}
			} else {
				removeTaskButton.setEnabled(false);
			}
		}
	};
	
	/** Tasks created by the user. */
	Set<Task> createdTasks;

	/**
	 * Start the GUI program.
	 * 
	 * @param args Command-line arguments.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				ScheduleToIpe sti = new ScheduleToIpe();
				sti.setVisible(true);
			}
		});
	}
	
	/**
	 * Create a new ScheduleToIpe GUI window.
	 */
	public ScheduleToIpe() {
		createdTasks = new HashSet<Task>();
		
		setTitle("ScheduleToIpe");
		setSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout());
		
		// Leftpanel
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		// list of created tasks
		taskListModel = new DefaultListModel<String>();
		taskListModel.addElement(NO_TASKS_TEXT);
		taskList = new JList<String>(taskListModel);
		taskList.addListSelectionListener(taskListSelectionListener);
		leftPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);
		// buttons to create and delete tasks
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(2, 1));
		addTaskButton = new JButton("Add task");
		addTaskButton.addActionListener(addTaskButtonListener);
		controlPanel.add(addTaskButton);
		removeTaskButton = new JButton("Remove selected task");
		removeTaskButton.addActionListener(removeTaskButtonListener);
		removeTaskButton.setEnabled(false);
		controlPanel.add(removeTaskButton);
		leftPanel.add(controlPanel, BorderLayout.PAGE_END);
		add(leftPanel);
		
		// Rightpanel
		taskInfoPane = new JTextPane();
		taskInfoPane.setEditable(false);
		taskInfoPane.setText(NO_TASKS_INFO_TEXT);
		add(taskInfoPane);
	}
}
