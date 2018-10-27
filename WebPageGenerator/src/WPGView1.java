import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * View class.
 *
 * @author Jason Xu (xu.2460)
 */
@SuppressWarnings("serial")
public final class WPGView1 extends JFrame implements WPGView {

    /**
     * Controller object registered with this view to observe user-interaction
     * events.
     */
    private WPGController controller;

    /**
     * State of user interaction: last event "seen".
     */
    private enum State {
        /**
         * Last event was clear, enter, another operator, or digit entry, resp.
         */
        SAW_NAME, SAW_ORGANIZATION, SAW_SUBPAGESNAME, SAW_SUBPAGESTEXT;
    }

    /**
     * State variable to keep track of which event happened last; needed to
     * prepare for digit to be added to bottom operand.
     */
    private State currentState;

    /**
     * Text areas.
     */
    private final JTextArea name, organization, subPagesName, subPagesText;

    /**
     * Operator and related buttons.
     */
    private final JButton bName, bOrganization, bSubPagesName, bSubPagesText,
            bPublish;

    /**
     * Useful constants.
     */
    private static final int TEXT_AREA_HEIGHT = 5, TEXT_AREA_WIDTH = 20,
            DIGIT_BUTTONS = 10, MAIN_BUTTON_PANEL_GRID_ROWS = 4,
            MAIN_BUTTON_PANEL_GRID_COLUMNS = 2, SIDE_BUTTON_PANEL_GRID_ROWS = 3,
            SIDE_BUTTON_PANEL_GRID_COLUMNS = 1, CALC_GRID_ROWS = 3,
            CALC_GRID_COLUMNS = 1;

    /**
     * Default constructor.
     */
    public WPGView1() {
        // Create the JFrame being extended

        /*
         * Call the JFrame (superclass) constructor with a String parameter to
         * name the window in its title bar
         */
        super("WebPageGenerator");

        // Set up the GUI widgets --------------------------------------------

        /*
         * Set up initial state of GUI to behave like last event was "Clear";
         * currentState is not a GUI widget per se, but is needed to process
         * digit button events appropriately
         */
        this.currentState = State.SAW_NAME;
        /*
         * Create widgets
         */
        this.name = new JTextArea("input your name here", TEXT_AREA_HEIGHT,
                TEXT_AREA_WIDTH);
        this.organization = new JTextArea("input your organization",
                TEXT_AREA_HEIGHT, TEXT_AREA_WIDTH);
        this.subPagesName = new JTextArea("input your subPagesName",
                TEXT_AREA_HEIGHT, TEXT_AREA_WIDTH);
        this.subPagesText = new JTextArea("input your subPagesText",
                TEXT_AREA_HEIGHT, TEXT_AREA_WIDTH);

        this.bName = new JButton("Confirm Name");
        this.bOrganization = new JButton("Confirm Organization");
        this.bSubPagesName = new JButton("Confirm SubPagesName");
        this.bSubPagesText = new JButton("Confrim SubPagesText");
        this.bPublish = new JButton("PUBLISH!");

        // Set up the GUI widgets --------------------------------------------

        /*
         * Text areas should wrap lines, and should be read-only; they cannot be
         * edited because allowing keyboard entry would require checking whether
         * entries are digits, which we don't want to have to do
         */
        this.name.setEditable(true);
        this.name.setLineWrap(true);
        this.name.setWrapStyleWord(true);

        this.organization.setEditable(true);
        this.organization.setLineWrap(true);
        this.organization.setWrapStyleWord(true);

        this.subPagesName.setEditable(true);
        this.subPagesName.setLineWrap(true);
        this.subPagesName.setWrapStyleWord(true);

        this.subPagesText.setEditable(true);
        this.subPagesText.setLineWrap(true);
        this.subPagesText.setWrapStyleWord(true);

        /*
         * Create scroll panes for the text areas in case number is long enough
         * to require scrolling
         */
        JScrollPane inputTextScrollPaneName = new JScrollPane(this.name);
        JScrollPane inputTextScrollPaneOrganization = new JScrollPane(
                this.organization);
        JScrollPane inputTextScrollPaneSubPagesName = new JScrollPane(
                this.subPagesName);
        JScrollPane inputTextScrollPaneSubPagesText = new JScrollPane(
                this.subPagesText);

        /*
         * Organize main window
         */
        this.setLayout(new GridLayout(5, 2));
        /*
         * Add scroll panes and button panel to main window, from left to right
         * and top to bottom
         */
        this.add(inputTextScrollPaneName);
        this.add(this.bName);
        this.add(inputTextScrollPaneOrganization);
        this.add(this.bOrganization);
        this.add(inputTextScrollPaneSubPagesName);
        this.add(this.bSubPagesName);
        this.add(inputTextScrollPaneSubPagesText);
        this.add(this.bSubPagesText);
        this.add(this.bPublish);

        // Set up the observers ----------------------------------------------

        /*
         * Register this object as the observer for all GUI events
         */
        this.bName.addActionListener(this);
        this.bOrganization.addActionListener(this);
        this.bSubPagesName.addActionListener(this);
        this.bSubPagesText.addActionListener(this);
        this.bPublish.addActionListener(this);

        // Set up the main application window --------------------------------

        /*
         * Make sure the main window is appropriately sized, exits this program
         * on close, and becomes visible to the user
         */
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        /*
         * Set cursor to indicate computation on-going; this matters only if
         * processing the event might take a noticeable amount of time as seen
         * by the user
         */
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        /*
         * Determine which event has occurred that we are being notified of by
         * this callback; in this case, the source of the event (i.e, the widget
         * calling actionPerformed) is all we need because only buttons are
         * involved here, so the event must be a button press; in each case,
         * tell the controller to do whatever is needed to update the model and
         * to refresh the view
         */
        Object source = event.getSource();
        if (source == this.bName) {
            this.controller.processNameEvent();
            this.currentState = State.SAW_NAME;
        } else if (source == this.bOrganization) {
            this.controller.processOrganizationEvent();
        }
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.bEnter) {
            this.controller.processEnterEvent();
            this.currentState = State.SAW_ENTER;
        } else if (source == this.bAdd) {
            this.controller.processAddEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.bSubtract) {
            this.controller.processSubtractEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.bMultiply) {
            this.controller.processMultiplyEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.bDivide) {
            this.controller.processDivideEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.bPower) {
            this.controller.processPowerEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.bRoot) {
            this.controller.processRootEvent();
            this.currentState = State.SAW_OTHER_OP;
        }
        }
/*
 * Set the cursor back to normal (because we changed it at the beginning of the
 * method body)
 */
this.setCursor(Cursor.getDefaultCursor());}}
