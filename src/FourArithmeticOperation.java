import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FourArithmeticOperation{
    private static Logger logger=Logger.getLogger("FourArithmeticOperation");

    public static void main(String[] args) {
        FourArithmeticOperation fourArithmeticOperation=new FourArithmeticOperation();
        List<String> resultList=new ArrayList<>();
        int count=fourArithmeticOperation.readValue();
        fourArithmeticOperation.outPutData(count,resultList);
        String[] strings=fourArithmeticOperation.inputResult();
        fourArithmeticOperation.checkTest(strings,resultList);
    }

    public int readValue(){
        int count=0;
        try {
            System.out.print("请输入你需要的算式个数：");
            Scanner scanner=new Scanner(System.in);
            count=scanner.nextInt();
            if(count<=0)throw new Exception();
        }catch (Exception e){
            logger.log(Level.WARNING,"数据输入错误，请输入整数并且大于0");
            readValue();
        }
        return count;
    }

    public String[] inputResult(){
        System.out.println("请输入您的答案并且以','分隔开来");
        Scanner scanner=new Scanner(System.in);
        String result=scanner.next();
        String[] strings=result.split(",");
        return strings;
    }

    public void checkTest(String[] strings,List<String> resultList){
        int count=0;
        for(int i=0;i<strings.length;i++){
            if(strings[i].equals(resultList.get(i))) count++;
        }
        System.out.print("答案：");
        for(String result:resultList){
            System.out.print(result+" , ");
        }
        System.out.println("");
        System.out.println("您的最终得分是："+computeScope(count,resultList.size()));
    }


    public void outPutData(int count,List<String> resultList){
        String sign[]={"+","-","×","÷"};
        String finallyResult="";
        for(int i=0;i<count;i++){
            List<String> signList=new ArrayList();
            List<String> arithmeticValue=new ArrayList<>();
            int signNumber=new Random().nextInt(2)+2;
            for(int j=0;j<signNumber;j++){
                int signIndex=new Random().nextInt(4);
                signList.add(sign[signIndex]);
            }
            int fraction=new Random().nextInt(signList.size());
            for(int k=0;k<=signList.size();k++){
                //
                if(k==fraction){
                    int denominator=new Random().nextInt(8)+2;
                    int molecule=new Random().nextInt(denominator)+1;
                    arithmeticValue.add("#"+molecule+"/"+denominator);
                }else {
                    int value=new Random().nextInt(9)+1;
                    arithmeticValue.add(Integer.toString(value));
                }
            }
            StringBuilder stringBuilder=new StringBuilder();
            for(int n=0;n<arithmeticValue.size();n++){
                if(arithmeticValue.get(n).startsWith("#")){
                    stringBuilder.append(arithmeticValue.get(n).substring(1));
                }else {
                    stringBuilder.append(arithmeticValue.get(n));
                }
                if(n==arithmeticValue.size()-1)break;
                stringBuilder.append(signList.get(n));
            }
            finallyResult=computeResult(signList,arithmeticValue);
            if(finallyResult.startsWith("#")){
                finallyResult=finallyResult.substring(1);
            }
            if(finallyResult.startsWith("-")){
                i--;
                continue;
            }else{
                resultList.add(fraction(finallyResult));
                System.out.println(stringBuilder.toString() + "  =  ");
            }
        }
    }

    public String computeResult(List<String> signList,List<String> arithmeticValue){
        Map<String,Integer> map=new HashMap<>();
        map.put("+",1);
        map.put("-",1);
        map.put("×",2);
        map.put("÷",2);
        Stack <String> numberValue=new Stack<>();
        Stack <String> signValue=new Stack<>();
        for(int i=0;i<signList.size();i++){
            if(i==0) {
                numberValue.push(arithmeticValue.get(i));
                signValue.push(signList.get(i));
            }else{
                numberValue.push(arithmeticValue.get(i));
                int thisLevel=map.get(signList.get(i));
                int thatLevel=map.get(signValue.peek());
                if(thisLevel>thatLevel){
                    signValue.push(signList.get(i));
                }else{
                    String oneStr=numberValue.pop();
                    String twoStr=numberValue.pop();
                    String resultStr=computeOptions(twoStr,oneStr,signValue.pop());
                    numberValue.push(resultStr);
                    signValue.push(signList.get(i));
                }
            }

        }
        numberValue.push(arithmeticValue.get(signList.size()));
        while (!signValue.isEmpty()){
            String oneStr=numberValue.pop();
            String twoStr=numberValue.pop();
            String resultStr=computeOptions(oneStr,twoStr,signValue.pop());
            numberValue.push(resultStr);
        }
        return numberValue.pop();
    }
    public String computeOptions(String one,String two, String options){
        String resultStr="";
        switch (options){
            case "+":
                if(one.startsWith("#")){
                    String oneMolecule=one.substring(1,one.indexOf("/"));
                    String oneDenominator=one.substring(one.indexOf("/")+1);
                    if(two.startsWith("#")){
                        String twoMolecule=two.substring(1,two.indexOf("/"));
                        String twoDenominator=two.substring(two.indexOf("/")+1);
                        int totalMolecule=Integer.parseInt(oneMolecule)*Integer.parseInt(twoDenominator)+
                                Integer.parseInt(twoMolecule)*Integer.parseInt(oneDenominator);
                        int totalDenominator=Integer.parseInt(oneDenominator)*Integer.parseInt(twoDenominator);
                        resultStr="#"+totalMolecule+"/"+totalDenominator;
                    }else{
                        int totalMolecule=Integer.parseInt(oneMolecule)+Integer.parseInt(oneDenominator)*Integer.parseInt(two);
                        resultStr="#"+String.valueOf(totalMolecule)+"/"+oneDenominator;
                    }
                }else if(!one.startsWith("#")&&two.startsWith("#")){
                    String twoMolecule=two.substring(1,two.indexOf("/"));
                    String twoDenominator=two.substring(two.indexOf("/")+1);
                    int totalMolecule=Integer.parseInt(twoMolecule)+Integer.parseInt(twoDenominator)*Integer.parseInt(one);
                    resultStr="#"+String.valueOf(totalMolecule)+"/"+twoDenominator;
                }else {
                    int resultAdd = Integer.parseInt(one) + Integer.parseInt(two);
                    resultStr = String.valueOf(resultAdd);
                }
                break;
            case "-":
                if(one.startsWith("#")){
                    String oneMolecule=one.substring(1,one.indexOf("/"));
                    String oneDenominator=one.substring(one.indexOf("/")+1);
                    if(two.startsWith("#")){
                        String twoMolecule=two.substring(1,two.indexOf("/"));
                        String twoDenominator=two.substring(two.indexOf("/")+1);
                        int totalMolecule=Integer.parseInt(oneMolecule)*Integer.parseInt(twoDenominator)-
                                Integer.parseInt(twoMolecule)*Integer.parseInt(oneDenominator);
                        int totalDenominator=Integer.parseInt(oneDenominator)*Integer.parseInt(twoDenominator);
                        resultStr="#"+totalMolecule+"/"+totalDenominator;
                    }else{
                        int totalMolecule=Integer.parseInt(oneMolecule)-Integer.parseInt(oneDenominator)*Integer.parseInt(two);
                        resultStr="#"+String.valueOf(totalMolecule)+"/"+oneDenominator;
                    }
                }else if(!one.startsWith("#")&&two.startsWith("#")){
                    String twoMolecule=two.substring(1,two.indexOf("/"));
                    String twoDenominator=two.substring(two.indexOf("/")+1);
                    int totalMolecule=Integer.parseInt(twoDenominator)*Integer.parseInt(one)-Integer.parseInt(twoMolecule);
                    resultStr="#"+String.valueOf(totalMolecule)+"/"+twoDenominator;
                }else {
                    int resultSubs = Integer.parseInt(one) - Integer.parseInt(two);
                    resultStr = String.valueOf(resultSubs);
                }
                break;
            case "×":
                if(one.startsWith("#")){
                    String oneMolecule=one.substring(1,one.indexOf("/"));
                    String oneDenominator=one.substring(one.indexOf("/")+1);
                    if(two.startsWith("#")){
                        String twoMolecule=two.substring(1,two.indexOf("/"));
                        String twoDenominator=two.substring(two.indexOf("/")+1);
                        int totalMolecule=Integer.parseInt(oneMolecule) *Integer.parseInt(twoMolecule);
                        int totalDenominator=Integer.parseInt(oneDenominator)*Integer.parseInt(twoDenominator);
                        resultStr="#"+totalMolecule+"/"+totalDenominator;
                    }else{
                        int totalMolecule=Integer.parseInt(oneMolecule)*Integer.parseInt(two);
                        resultStr="#"+String.valueOf(totalMolecule)+"/"+oneDenominator;
                    }
                }else if(!one.startsWith("#")&&two.startsWith("#")){
                    String twoMolecule=two.substring(1,two.indexOf("/"));
                    String twoDenominator=two.substring(two.indexOf("/")+1);
                    int totalMolecule=Integer.parseInt(twoMolecule)*Integer.parseInt(one);
                    resultStr="#"+String.valueOf(totalMolecule)+"/"+twoDenominator;
                }else {
                    int resultSubs = Integer.parseInt(one) * Integer.parseInt(two);
                    resultStr = String.valueOf(resultSubs);
                }
                break;
            case "÷":
                if(one.startsWith("#")){
                    String oneMolecule=one.substring(1,one.indexOf("/"));
                    String oneDenominator=one.substring(one.indexOf("/")+1);
                    if(two.startsWith("#")){
                        String twoMolecule=two.substring(1,two.indexOf("/"));
                        String twoDenominator=two.substring(two.indexOf("/")+1);
                        int totalMolecule=Integer.parseInt(oneMolecule) *Integer.parseInt(twoDenominator);
                        int totalDenominator=Integer.parseInt(oneDenominator)*Integer.parseInt(twoMolecule);
                        resultStr="#"+totalMolecule+"/"+totalDenominator;
                    }else{
                        int totalMolecule=Integer.parseInt(oneMolecule);
                        int totalDenominator=Integer.parseInt(oneDenominator)*Integer.parseInt(two);
                        resultStr="#"+String.valueOf(totalMolecule)+"/"+totalDenominator;
                    }
                }else if(!one.startsWith("#")&&two.startsWith("#")){
                    String twoMolecule=two.substring(1,two.indexOf("/"));
                    String twoDenominator=two.substring(two.indexOf("/")+1);
                    int totalMolecule=Integer.parseInt(twoDenominator)*Integer.parseInt(one);
                    int totalDenominator=Integer.parseInt(twoDenominator);
                    resultStr="#"+String.valueOf(totalMolecule)+"/"+twoDenominator;
                }else {
                    resultStr="#"+one+"/"+two;
                }
                break;

        }
        return resultStr;
    }

    public String fraction(String finallyResult){
        String fr="";
        String oneMolecule=finallyResult.substring(0,finallyResult.indexOf("/"));
        String oneDenominator=finallyResult.substring(finallyResult.indexOf("/")+1);
        int smaller = Integer.parseInt(oneMolecule) > Integer.parseInt(oneDenominator) ? Integer.parseInt(oneDenominator) : Integer.parseInt(oneMolecule);
        int commonFactor=1;
        for (int i = 1; i <= smaller; i++) {
            if ( Integer.parseInt(oneMolecule) % i == 0 && Integer.parseInt(oneDenominator) % i == 0) {
                commonFactor = i;
            }
        }
        if(Integer.parseInt(oneDenominator)/commonFactor==1){
            return Integer.parseInt(oneMolecule)/commonFactor+"";
        }else {
            return Integer.parseInt(oneMolecule) / commonFactor + "/" + Integer.parseInt(oneDenominator) / commonFactor;
        }
    }

    public String computeScope(int num1,int num2){
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        String result = numberFormat.format((float) num1 / (float) num2 * 100);
        return result;
    }
}

