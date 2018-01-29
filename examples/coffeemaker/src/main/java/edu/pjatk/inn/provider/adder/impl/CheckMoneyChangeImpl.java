package edu.pjatk.inn.provider.adder.impl;

import edu.pjatk.inn.provider.adder.CheckMoneyChange;
import sorcer.core.context.Contexts;
import sorcer.core.context.PositionalContext;
import sorcer.service.Context;
import sorcer.service.ContextException;

import java.util.List;

public class CheckMoneyChangeImpl implements CheckMoneyChange{
    private int COIN_10G = 0;
    private int COIN_20G = 0;
    private int COIN_50G = 0;
    private int COIN_1Z = 0;
    private int COIN_2Z = 0;
    private int COIN_5Z = 0;
    int[] outputCoins = {0,0,0,0,0,0};

    public CheckMoneyChangeImpl(int COIN_10G, int COIN_20G, int COIN_50G, int COIN_1Z, int COIN_2Z, int COIN_5Z) {
        this.COIN_10G = COIN_10G * 10;
        this.COIN_20G = COIN_20G * 20;
        this.COIN_50G = COIN_50G * 50;
        this.COIN_1Z = COIN_1Z * 100;
        this.COIN_2Z = COIN_2Z * 200;
        this.COIN_5Z = COIN_5Z * 500;
    }


    @Override
    public Context checkMoneyChange(Context context) throws Exception {
        PositionalContext out = (PositionalContext) context;

        List<Integer> inputs = determineInputs(out);
        int price = inputs.get(0);
        int IN_COIN_10G = inputs.get(1);
        int IN_COIN_20G = inputs.get(2);
        int IN_COIN_50G = inputs.get(3);
        int IN_COIN_1Z = inputs.get(4);
        int IN_COIN_2Z = inputs.get(5);
        int IN_COIN_5Z = inputs.get(6);
        int inputCoin = IN_COIN_10G*10+IN_COIN_20G*20+IN_COIN_50G*50+IN_COIN_1Z*100+IN_COIN_2Z*200+IN_COIN_5Z*500;
        if(inputCoin == price)
            out.setReturnValue(0);
        else if(inputCoin < price)
            out.setReturnValue("Sorry not enough money");
        else {
            int tmp = inputCoin - price;
            Z5(tmp);
            out.setReturnValue(outputCoins);
        }
        return out;
    }

    private int isNot0Exist(int[] arr){
        for(int i = arr.length; i >=0; i--){
            if(i != 0)
                return i;
        }
        return -1;
    }

    private boolean isEnougth(int COIN, int cel){
        return COIN >= cel;
    }

    private void Z5(int tmp) throws Exception {
        int cel5Zt = tmp/500;
        int cel5Z = cel5Zt*500;
        if(cel5Z <= COIN_5Z){
            COIN_5Z -= cel5Z;
            outputCoins[5] = cel5Zt;
            tmp -= cel5Z;
            Z2(tmp);
        } else{
            tmp -= COIN_5Z;
            COIN_5Z = 0;
            Z2(tmp);
        }
    }

    private void Z2(int tmp) throws Exception {
        int cel2Zt = tmp/200;
        int cel2Z = cel2Zt*200;
        if(cel2Z <= COIN_2Z){
            COIN_2Z -= cel2Z;
            outputCoins[4] = cel2Zt;
            tmp -= cel2Z;
            Z1(tmp);
        } else{
            tmp -= COIN_2Z;
            COIN_2Z = 0;
            Z1(tmp);
        }
    }

    private void Z1(int tmp) throws Exception {
        int cel1Zt = tmp/100;
        int cel1Z = cel1Zt*100;
        if(cel1Z <= COIN_1Z){
            COIN_1Z -= cel1Z;
            outputCoins[3] = cel1Zt;
            tmp -= cel1Z;
            G50(tmp);
        } else{
            tmp -= COIN_1Z;
            COIN_1Z = 0;
            G50(tmp);
        }
    }

    private void G50(int tmp) throws Exception {
        int cel50Gt = tmp/50;
        int cel50G = cel50Gt*50;
        if(cel50G <= COIN_50G){
            COIN_50G -= cel50G;
            outputCoins[2] = cel50G;
            tmp -= cel50G;
            G20(tmp);
        } else{
            tmp -= COIN_50G;
            COIN_50G = 0;
            G20(tmp);
        }
    }
    private void G20(int tmp) throws Exception {
        int cel20Gt = tmp/20;
        int cel20G = cel20Gt*20;
        if(cel20G <= COIN_20G){
            COIN_20G -= cel20G;
            outputCoins[1] = cel20G;
            tmp -= cel20G;
            G10(tmp);
        } else{
            tmp -= COIN_20G;
            COIN_20G = 0;
            G10(tmp);
        }
    }
    private void G10(int tmp) throws Exception {
        int cel10Gt = tmp/10;
        int cel10G = cel10Gt*10;
        if(cel10G <= COIN_10G){
            COIN_10G -= cel10G;
            outputCoins[0] = cel10G;
            tmp -= cel10G;
        } else
            throw new Exception("not enougth money");
    }

    private List<Integer> determineInputs(PositionalContext context) throws ContextException {
        List<Integer> inputs = (List<Integer>) Contexts.getNamedInValues(context);
        if (inputs == null || inputs.size() == 0) {
            inputs = (List<Integer>) Contexts.getPrefixedInValues(context);
//				logger.info("prefixed inputs: \n" + inputs);
        }
        //logger.info("named inputs: \n" + inputs);
        if (inputs == null || inputs.size() == 0)
            inputs = (List<Integer>) context.getInValues();
        if (inputs == null || inputs.size() == 0) {
            if (context.size() == 2) {
                inputs.add((Integer) context.getValueAt(0));
                inputs.add((Integer) context.getValueAt(1));
//				logger.info("first: \n" + context.getValueAt(0));
//				logger.info("second: \n" + context.getValueAt(1));
            }
        }
        if (inputs != null && inputs.size() < 2) {
            inputs = context.getAllInValues();
        }
        return inputs;
    }
}
