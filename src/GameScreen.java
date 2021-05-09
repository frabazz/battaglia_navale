import javax.swing.*;
import java.awt.*;

public class GameScreen {
    Container container;
    Player bot, player;
    int[] boatsLength;
    int width, height, cellSize;
    JLabel playerLabel, botLabel, scoreLabel;
    JButton button;
    JFrame frame;
    GraphicGrid PlayerGrid, BotGrid;
    int[][] originalM;
    int botScore=0, playerScore=0, round = 1;
    public void setContainer(Container container) {
        this.container = container;
    }

    public Player getBot() {
        return bot;
    }

    public void setBot(Player bot) {
        this.bot = bot;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    GameScreen(JFrame frame, String PlayerName, String BotName, int[] boatsLength, int width, int height, int cellSize) {
        this.cellSize = cellSize;
        this.frame = frame;
        this.container = frame.getContentPane();
        this.bot = new Player(BotName, width, height, boatsLength, true);
        this.originalM = this.bot.getMatrix();
        this.player = new Player(PlayerName, width, height, boatsLength);
        this.boatsLength = boatsLength;
        this.width = width;
        this.height = height;
    }

    private GridBagConstraints getConstraints(int gridx, int gridy, int weightx, int weighty, int position) {
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = gridx;
        grid.gridy = gridy;
        grid.fill = position;
        grid.weightx = weightx;
        grid.weighty = weighty;
        return grid;
    }

    private void setupButton() {
        button = new JButton("azione");
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(150, 100));
        button.setBackground(Color.BLUE);
        button.setContentAreaFilled(true);
        button.setForeground(Color.WHITE);
        container.add(button, getConstraints(1, 1, 1, 1, GridBagConstraints.CENTER));
    }

    private void setupPlayerLabel() {
        playerLabel = new JLabel("label del giocatore");
        playerLabel.setForeground(Color.BLUE);
        playerLabel.setFont(new Font("TimesRoman", Font.PLAIN, 22));
        container.add(playerLabel, getConstraints(0, 0, 2, 1, GridBagConstraints.FIRST_LINE_START));
    }

    private void setupScoreLabel() {
        scoreLabel = new JLabel("0-0");
        scoreLabel.setFont(new Font("TimesRoman", Font.PLAIN, 24));
        container.add(scoreLabel, getConstraints(1, 0, 1, 1, GridBagConstraints.PAGE_START));
    }

    private void setupBotLabel() {
        botLabel = new JLabel("");
        botLabel.setFont(new Font("TimesRoman", Font.PLAIN, 22));
        botLabel.setForeground(Color.BLACK);
        container.add(botLabel, getConstraints(2, 0, 2, 1, GridBagConstraints.FIRST_LINE_END));
    }

    public void setPlayerError(String textError) {
        playerLabel.setForeground(Color.RED);
        playerLabel.setText(textError);
    }

    public void setPlayerMessage(String text) {
        playerLabel.setForeground(Color.BLACK);
        playerLabel.setText(text);
    }

    public void setBotMessage(String text){
        botLabel.setForeground(Color.BLACK);
        botLabel.setText(text);
    }

    public void setBotError(String text){
        botLabel.setForeground(Color.RED);
        botLabel.setText(text);
    }

    public void updateRoundButton(){
        button.setForeground(Color.white);
        button.setText("round " + round);
    }

    public void updateScoreLabel(){
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setText((playerScore+"-"+botScore));
    }

    public void setupComponents() {
        GridBagLayout layout = new GridBagLayout();
        container.setLayout(layout);
        setupPlayerLabel();
        setupScoreLabel();
        setupBotLabel();
        PlayerGrid = new GraphicGrid(player.getMatrix(), cellSize, boatsLength);
        PlayerGrid.setPreferredSize(new Dimension(12 * cellSize, 12 * cellSize));
        container.add(PlayerGrid, getConstraints(0, 1, 1, 1, GridBagConstraints.LINE_START));
        setupButton();
        BotGrid = new GraphicGrid(bot.getMatrix(), cellSize, true);
        BotGrid.setPreferredSize(new Dimension(12 * cellSize, 12 * cellSize));
        container.add(BotGrid, getConstraints(2, 1, 1, 1, GridBagConstraints.LINE_END));
        frame.setVisible(true);
    }

    private int[] getCoordinates() {
        int[] coordinates = BotGrid.selectEnemyCell();
        while (coordinates[0] == -1 || coordinates[1] == -1) {
            setPlayerMessage("Inserisci correttamente le coordinate");
            coordinates = BotGrid.selectEnemyCell();
        }
        return coordinates;
    }

    public int gameLoop() throws InterruptedException {
        updateRoundButton();
        for (int length : boatsLength) {
            setPlayerMessage("sistema la barca " + length);
            while (!PlayerGrid.selectBoat(player, length)) setPlayerError("errore nel posizionamento, riprova");
            setPlayerMessage("perfetto");
        }
        Boat[] selectedBoats = PlayerGrid.getBoats();
        for(int i = 0;i < selectedBoats.length;i++){
            player.addBoatIndex(selectedBoats[i], i);
        }
        setPlayerMessage("Barche posizionate correttamente");
        PlayerGrid.removeListeners();
        Thread.sleep(1500);
        while ((!player.hasLost() || !bot.hasLost()) && round<=20) {
            round++;
            updateRoundButton();
            setPlayerMessage("seleziona una casella nemica : ");
            int[] coordinates = getCoordinates();
            if (bot.isBoat(coordinates[0], coordinates[1])) {
                BotGrid.sinkBoat(bot.getBoat(coordinates[0], coordinates[1]));
                bot.sinkBoat(coordinates[0], coordinates[1]);
                setPlayerMessage("Hai affondato la barca nemica!");
                playerScore++;
                updateScoreLabel();
            }
            else{
                bot.setWater(coordinates[0], coordinates[1]);
                BotGrid.setWater(coordinates[0], coordinates[1]);
                setPlayerMessage("Acqua!");
            }
            Thread.sleep(1000);
            if(bot.hasLost())break;
            setBotMessage("adesso tocca a me");
            Thread.sleep(1000);
            int[] botCoordinates = bot.getRandomCoordinates();
            if(player.isBoat(botCoordinates[0], botCoordinates[1])){
                PlayerGrid.sinkBoat(player.getBoat(botCoordinates[0], botCoordinates[1]));
                player.sinkBoat(botCoordinates[0], botCoordinates[1]);
                setBotMessage("ti ho affondato la barca!");
                botScore++;
                updateScoreLabel();
                Thread.sleep(1000);
            }
            else{
                PlayerGrid.setWater(botCoordinates[0], botCoordinates[1]);
                player.setWater(botCoordinates[0], botCoordinates[1]);
                setBotMessage("Acqua");
                Thread.sleep(1000);
            }
        }
        String messagelost = player.hasLost()? "peccato hai perso! ricominciare?" : "complimenti hai vinto! ricominciare?";
        if(!player.hasLost() && !bot.hasLost())messagelost = "sono finiti i round!ricominciare!";
        int decision =  JOptionPane.showConfirmDialog (null, messagelost,"Ricominciare?",JOptionPane.YES_NO_OPTION);
        if(decision == JOptionPane.YES_OPTION){
            frame.getContentPane().removeAll();
            frame.repaint();
            frame.setVisible(false);
        }
        return decision;
    }
}
