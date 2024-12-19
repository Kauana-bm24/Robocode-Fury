package sample;

import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import java.awt.Color;

public class paredecorre extends Robot {
    
    private double moveAmount; // Para controlar a quantidade de movimento

    /**
     * run: Move around the walls
     */
    public void run() {
        setBodyColor(Color.black);
        setGunColor(Color.black);
        setRadarColor(Color.orange);
        setBulletColor(Color.cyan);
        setScanColor(Color.cyan);

        // Inicializa o valor de moveAmount para o tamanho máximo da arena
        moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());

        // Faz o robô se mover em volta das paredes
        turnLeft(getHeading() % 90); // Ajusta o heading para o múltiplo mais próximo de 90
        ahead(moveAmount); // Move para frente até a borda da arena
        turnGunRight(90); // Gira a arma 90 graus para frente
        turnRight(90); // Gira o corpo 90 graus

        while (true) {
            // Realiza o movimento contínuo ao redor da arena
            ahead(moveAmount); // Move para frente
            turnRight(90); // Faz uma curva de 90 graus
        }
    }

    /**
     * onHitRobot: Move away a bit if hitting another robot
     */
    public void onHitRobot(HitRobotEvent e) {
        // Se o robô está na frente, move para trás
        if (e.getBearing() > -90 && e.getBearing() < 90) {
            back(100);
        } else {
            // Se o robô está atrás, move para frente
            ahead(100);
        }
    }

    /**
     * onScannedRobot: Verifica o número de inimigos detectados e toma ações baseadas nisso
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        int robocontagem = getOthers(); // Obtém o número de inimigos restantes

        // Se houver mais de dois inimigos, o robô começa a atirar
        if (robocontagem > 2) {
            turnGunRight(e.getBearing()); // Gira a arma para o inimigo
            fire(3); // Dispara com potência 3
            ahead(100); // Move para frente
        } else {
            // Caso contrário, o robô apenas gira a arma em busca de inimigos
            turnGunRight(360); // Gira a arma 360 graus
        }
    }
}