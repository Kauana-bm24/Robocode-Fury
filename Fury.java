package FuryICO;
import robocode.*;
import robocode.util.Utils;
import java.awt.Color;

public class Fury extends AdvancedRobot {
    private int robotCount = 0;
    private boolean aggressiveMode = false;  // Variável para controlar a estratégia de ataque
    private boolean defensiveMode = false; // Estratégia defensiva
    private double moveAmount; // Quantidade de movimento
    private boolean peek; // Controle para "escanear" a frente

    public void run() {
        setBodyColor(Color.black);
        setGunColor(Color.black);
        setRadarColor(Color.orange);
        setBulletColor(Color.cyan);
        setScanColor(Color.cyan);

        // Inicializa os parâmetros da estratégia defensiva
        moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
        peek = false;

        while (true) {
            robotCount = getOthers();  // Atualiza o número de robôs restantes

            if (robotCount > 2) {
                // Estratégia defensiva: mover paralelamente às paredes
                defensiveMode = true;
                moveParallelToWalls();
            } else {
                // Estratégia de ataque
                defensiveMode = false;
                aggressiveMode = true;
                attackEnemies();
            }
            execute(); // Executa as ações
        }
    }

    // Estratégia de movimento paralelamente às paredes
    private void moveParallelToWalls() {
        // Calcula a distância até a parede mais próxima
        double safeDistance = 20;  // Distância mínima segura da parede (ajustável)
        double x = getX();
        double y = getY();

        // Verifica se o robô está perto da borda esquerda ou direita
        if (x < safeDistance) {
            setTurnRight(90);  // Se está muito perto da borda esquerda, vira para a direita
            setAhead(100);  // Move para frente, paralelamente à parede
        } else if (x > getBattleFieldWidth() - safeDistance) {
            setTurnLeft(90);  // Se está muito perto da borda direita, vira para a esquerda
            setAhead(100);  // Move para frente, paralelamente à parede
        }
        
        // Verifica se o robô está perto da borda superior ou inferior
        else if (y < safeDistance) {
            setTurnRight(90);  // Se está muito perto da borda superior, vira para a direita
            setAhead(100);  // Move para frente, paralelamente à parede
        } else if (y > getBattleFieldHeight() - safeDistance) {
            setTurnLeft(90);  // Se está muito perto da borda inferior, vira para a esquerda
            setAhead(100);  // Move para frente, paralelamente à parede
        } else {
            // Se não estiver perto de nenhuma parede, move-se ao longo de uma borda, como padrão
            setTurnRight(90);
            ahead(moveAmount);
        }
    }

    // Estratégia de ataque
    private void attackEnemies() {
        // O radar só gira 30° para detectar os inimigos
        setTurnRadarRight(30); // Radar variando entre 30°
        
        // A arma deve seguir o movimento do radar
        setTurnGunRight(30); // Gira a arma no mesmo ângulo do radar
        
        execute();
    }

    // Comportamento quando um inimigo é escaneado
    public void onScannedRobot(ScannedRobotEvent e) {
        // Verifica a distância do inimigo
			double enemyBearing = getHeading() + e.getBearing(); // Direção do inimigo
			double enemyDistance = e.getDistance(); // Distância até o inimigo
			double myHeading = getHeading(); // Direção atual do robô

			// Gira o robô para alinhar com o inimigo
			double turnToEnemy = Utils.normalRelativeAngleDegrees(enemyBearing - myHeading);
			setTurnRight(turnToEnemy);  // Gira o corpo
			setTurnGunRight(Utils.normalRelativeAngleDegrees(enemyBearing - getGunHeading()));  // Gira a arma

			// Move-se em direção ao inimigo
			setAhead(enemyDistance - 50); // Avança em direção ao inimigo

			// Atira de acordo com a distância
			if (enemyDistance < 50) {
				fire(3); // Dispara com força máxima quando perto
			} else {
				fire(1); // Dispara com menos força quando distante
			}
    }

    // Comportamento quando o robô é atingido por uma bala (somente em modo agressivo)
    public void onHitByBullet(HitByBulletEvent e) {
        if (aggressiveMode) {
            double bulletBearing = e.getBearing(); // Direção da bala
            double escapeAngle = bulletBearing + 180; // Direção oposta ao disparo
            escapeAngle = Utils.normalRelativeAngleDegrees(escapeAngle); // Normaliza o ângulo

            // Move-se para longe da direção do tiro
            setTurnRight(escapeAngle);  // Gira o robô para se afastar da direção do tiro
            setAhead(200);  // Move-se para longe
            fire(1);  // Atira uma vez para se defender
        }
    }

    // Quando o robô colide com outro robô (somente em modo agressivo)
    public void onHitRobot(HitRobotEvent e) {
        if (aggressiveMode) {
            // Se o robô está na frente, move-se para trás
            if (e.getBearing() > -90 && e.getBearing() < 90) {
                back(100);
            } else {
                ahead(100);
            }
        }
    }
}

