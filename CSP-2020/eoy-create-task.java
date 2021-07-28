// Libraries are not mine but required for program functionality.
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*; // Not mine, this program uses the JSWING library for GUI capabilities.
public class mathMaddness {
    private String title = "Math Madness!"; // Title - Game title
    private int score = 0; // Score - Global variable that will store the score fot the current user. (Current object instance.)
    int correctAnswer; // Correct answer - Global variable that will store the current correct answer. (Current object instance.)
    private String username = ""; // Username - Global variable that is set to the user-supplied username for the current object instance.
    public static String[] leaderboard = new String[9]; // Top 10 players - static so it is assigned to the current class and can be accessed when creating a new object.
    int placeValues = leaderboard.length - 1;
    public static int defaultCount = 1;
    Random rand = new Random(); // Random object - Object to access random library functions in the current object instance.
    JLabel equationText = new JLabel(); // Equation - Displays the current equation.
    JTextField answerInput = new JTextField(10); // Answer Input - Access the user input for the answer.
    JButton nextButton = new JButton("Next"); // Next Button - Submit the user input specified in the above variable.
    public static void main(String[] args) {
        //This method spawns the process on an interactable thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Call constructor upon execution
                new mathMaddness();
            }
        });
    }
    //Configure frame method
    static void configureFrame(JFrame frm) {
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setSize(500, 500);
        frm.setMinimumSize(new Dimension(500, 500));
        frm.setMaximumSize(new Dimension(500, 500));
        frm.setLayout(new FlowLayout());
        frm.setResizable(false);
    }
    //Game Logic
    int num1, num2;
    public void mainLogic(String operator) {
        num1 = rand.nextInt(100); // Select random first operand
        num2 = rand.nextInt(100); // Select random second operand
        switch (operator) {
            // Switch statement to set the correctAnswer value of current instance to its corresponding equation.
            case "+":
                correctAnswer = num1 + num2;
                break;
            case "-":
                correctAnswer = num1 - num2;
                break;
            case "*":
                correctAnswer = num1 * num2;
                break;
        }
        while (correctAnswer < 0) {
            mainLogic(operator); //No negative answers allowedmainLogic(operator); //No negative answers allowed
        }
        equationText.setText(Integer.toString(num1) + operator + Integer.toString(num2)); // Display the equation.
    }
    // Final screen - Shows results and other users' scores.
    void showResults(JFrame frame) { // Takes a frame object.
        frame.getContentPane().removeAll(); // Remove all previous components from the frame.
        JLabel titleText = new JLabel("Scores:"); // Scores - Text to denote scores area.
        JPanel pane = new JPanel(); // Panel - Panel to sort components in the frame vertically.
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS)); // Boxlayout will set the components vertically as the Y_AXIS is specified.
        JLabel[] rows = new JLabel[9]; // Instantiate 9 text labels that will hold the value of other users' scores.
        JButton replayButton = new JButton("Play Again"); // Restart button.
        ///////vvv ADD COMPONENTS TO PANE vvv///////
        titleText.setFont(titleText.getFont().deriveFont(60.0 f));
        pane.add(titleText);
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new JLabel("");
            rows[i].setFont(rows[i].getFont().deriveFont(20.0 f));
            pane.add(rows[i]);
        }
        ///////^^^ ADD COMPONENTS TO PANE ^^^///////
        for (int i = placeValues; i > 0; i--) {
            // Configure leaderboard values to move up (this is to make room for the new/current user)
            leaderboard[i] = leaderboard[i - 1];
        }
        leaderboard[0] = username + " | " + Integer.toString(score); // Append the current user to the first value on the leaderboard
        for (int i = 0; i < placeValues; i++) {
            // Assign the rows text labels the values on the leaderboard array.
            rows[i].setText(leaderboard[i]);
        }
        replayButton.addActionListener(new ActionListener() {
            @Override
            // If the user clicks the replay button then..
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Send the current frame to the garbage collector.
                mathMaddness newgame = new mathMaddness(); // Instantiate a new mathMaddness object
            }
        });
        JLabel newline;
        pane.add(newline = new JLabel(" ")); // New line object, makes it look nice
        pane.add(replayButton); // Add restart button at the bottom
        frame.add(pane); // Add the pane to the frame
        // Finish
        frame.setVisible(true);
    }
    String lastOp;
    void mainGame() {
        JFrame gameFrame = new JFrame(title); // Create a new frame for the round
        JLabel scoreText = new JLabel("Score: 0"); // Create a label to display the score, default to show 0.
        ///////vvv Configure Frame vvv///////
        equationText.setFont(equationText.getFont().deriveFont(50.0 f));
        configureFrame(gameFrame);
        gameFrame.add(equationText);
        gameFrame.add(answerInput);
        gameFrame.add(nextButton);
        gameFrame.add(scoreText);
        ///////^^^ Configure Frame ^^^//////////////^^^ Configure Frame ^^^///////
        String[] ops = {
            "+",
            "-",
            "*"
        }; // List of arithmatic operations
        lastOp = ops[rand.nextInt(ops.length)]; //No repeating operations
        mainLogic(lastOp); // Create equation one.
        // Handle "next" button click (check the equation in the answerInput).
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (answerInput.getText().matches("\\d+")) { // Check if the string is a number using regular expressions.
                    int supplied = Integer.parseInt(answerInput.getText()); // String is a number so create an integer out of it.
                    if (supplied == correctAnswer) { // If user supplied number is the same as the correct answer then..
                        score++; // Increase score.
                        answerInput.setText(""); // Empty user input box.
                        scoreText.setText("Score: " + Integer.toString(score)); // Display new score.
                        String newOp = ops[rand.nextInt(ops.length)];
                        while (newOp == lastOp) {
                            newOp = ops[rand.nextInt(ops.length)];
                        }
                        mainLogic(newOp); // Create another equation.
                        lastOp = newOp;
                    } else {
                        // User supplied input is a number, but the answer is wrong.
                        System.out.println("Scored " + Integer.toString(score));
                        gameFrame.setVisible(false);
                        showResults(gameFrame); // Show results
                    }
                } else {
                    // User supplied input is not a number and by default is wrong.
                    System.out.println("Scored " + Integer.toString(score));
                    gameFrame.setVisible(false);
                    showResults(gameFrame); // Show results
                }
            }
        });
        // Finish
        gameFrame.setVisible(true);
    }
    // Constructor (MAIN MENU)
    mathMaddness() {
        JFrame menuFrame = new JFrame(title); // Create frame for main menu
        //Components
        JLabel titleText = new JLabel(title);
        JLabel rulesText = new JLabel("Answer as many equations as possible correctly to increase your score.");
        JButton playButton = new JButton("Start");
        JLabel prompt1Text = new JLabel("Enter a username (5 chars max):");
        JTextField nameInput = new JTextField(5);
        //Configure frame
        configureFrame(menuFrame);
        ///////vvv Configure Components vvv///////
        titleText.setFont(titleText.getFont().deriveFont(50.0 f));
        rulesText.setFont(rulesText.getFont().deriveFont(13.0 f));
        playButton.setPreferredSize(new Dimension(400, 50));
        menuFrame.add(titleText);
        menuFrame.add(rulesText);
        menuFrame.add(prompt1Text);
        menuFrame.add(nameInput);
        menuFrame.add(nameInput);
        menuFrame.add(playButton);
        ///////^^^ Configure Components ^^^///////
        // Handle "play" button click
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                score = 0;
                //Check specified name input
                if (nameInput.getText().length() > 5) { // Check if username exceeds 5 chars.
                    username = nameInput.getText().substring(0, 5); //Username specified exceeds limit, only save first 5 chars.
                } else if (nameInput.getText().length() == 0) { // Check if no username was supplied.
                    username = "New Player " + Integer.toString(defaultCount); //No username was specified, default to this username.
                    defaultCount++;
                } else {
                    username = nameInput.getText(); //Username specified meets requirements.
                }
                System.out.println(username + " is the username set!");
                menuFrame.setVisible(false);
                mainGame(); // Start game
            }
        });
        // Finish
        menuFrame.setVisible(true);
    }
}
