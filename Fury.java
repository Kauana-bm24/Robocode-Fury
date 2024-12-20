package FuryICO;
import robocode.*;
import robocode.util.Utils;

public class Fury extends AdvancedRobot {
	private int robotCount = 0;
	private boolean aggressiveMode = false;  // Variável para controlar a estratégia de ataque

	public void run() {
    	setAdjustGunForRobotTurn(true);  // Permite que a arma gire independentemente do corpo do robô
    	setAdjustRadarForRobotTurn(true);  // Permite que o radar gire independentemente do corpo do robô

    	while (true) {
        	// Gire o radar para escanear a área, mas com rotação menor
        	setTurnRadarRight(15);  // Gira o radar um pouco de cada vez
        	execute();  // Executa as ações
    	}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
    	robotCount = getOthers(); // Atualiza o número de robôs restantes

    	double enemyBearing = getHeading() + e.getBearing(); // Direção absoluta do inimigo
    	double enemyDistance = e.getDistance(); // Distância até o inimigo
    	double myHeading = getHeading(); // Direção atual do robô

    	// Verifica se o inimigo está muito próximo (<10)
    	if (enemyDistance < 10) {
        	aggressiveMode = true;  // Ativa o modo agressivo de ataque
    	}

    	if (aggressiveMode) {
        	// Estratégia de ataque quando o inimigo está muito perto
        	double turnToEnemy = Utils.normalRelativeAngleDegrees(enemyBearing - myHeading);
        	setTurnRight(turnToEnemy);  // Gira o robô para alinhar com o inimigo
        	setTurnGunRight(Utils.normalRelativeAngleDegrees(enemyBearing - getGunHeading()));  // Gira a arma para o inimigo

        	setAhead(enemyDistance - 5);  // Move para pressionar o inimigo (muito próximo)

        	fire(3);  // Dispara com força máxima contra o inimigo

        	// Caso o inimigo seja eliminado ou não esteja mais perto
        	if (enemyDistance > 10) {
            	aggressiveMode = false;  // Desativa o modo agressivo
        	}
    	} else {
        	// Estratégia normal (perseguir ou fugir)
        	double turnToEnemy = Utils.normalRelativeAngleDegrees(enemyBearing - myHeading);
        	setTurnRight(turnToEnemy);  // Gira o robô para alinhar com o inimigo

        	// Gira a arma para apontar diretamente para o inimigo
        	setTurnGunRight(Utils.normalRelativeAngleDegrees(enemyBearing - getGunHeading()));

        	// Evitar bater nas bordas da arena
        	if (getX() < 50 || getX() > getBattleFieldWidth() - 50 || getY() < 50 || getY() > getBattleFieldHeight() - 50) {
            	// Se estiver perto das bordas, gira para desviar
            	setTurnRight(90);  // Gire para desviar
            	setAhead(100); 	// Ande para longe da borda
        	} else {
        	
            	// Se houver menos de 3 robôs, ataque
            	if (robotCount <= 2) {
                	setAhead(enemyDistance - 50);  // Avança para ficar mais próximo do inimigo

                	// Atira dependendo da distância
                	if (enemyDistance < 200) {
                    	fire(3);  // Dispara com força máxima quando perto
                	} else {
                    	fire(1);  // Dispara com menos força quando longe
                	}
            	} else {
                	// Fugir quando houver mais de 2 inimigos
                	setBack(100);  // Afasta-se
                	fire(1);  // Atira apenas uma vez para se defender
                	-Djava.security.manager=allow \
            	}
        	}
    	}
	}
}

