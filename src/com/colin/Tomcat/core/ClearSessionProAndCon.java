package com.colin.Tomcat.core;

import com.colin.Tomcat.impl.TomcatHttpSession;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 2024年06月27日13:38
 */
public class ClearSessionProAndCon {

    /**
     * 用于开关生产者和消费者工作的标记
     */
    private volatile boolean flag = true;

    /**
     * 使用阻塞队列的接口引用
     * <p>
     * 为了适配上层调用者的任意阻塞队列实例
     */
        private BlockingQueue<Integer> blockingQueue;

    public ClearSessionProAndCon(BlockingQueue<Integer> blockingQueue){
        this.blockingQueue = blockingQueue;
    }


    public void doProduct() throws InterruptedException {
        while (this.flag){
            Random random = new Random();
            //将session容器中的key 也就是SessionId 转换为Set集合
            Set<Integer> sessionIdSet = SessionManager.sessionContainer.keySet();
            //将set集合转换为一个数组，数组中的所有元素是SessionId
            Integer[] sessionIdSetArray = sessionIdSet.toArray(new Integer[0]);
            //可以添加一个判断，根据当前SessionId数量，取模几个阈值，来决定每一次抽样的数量
            //每次检查的数量
            int checkNum;
            //间隔的时间
            int scheduledTime;
            int threshold = sessionIdSetArray.length % 500;
            switch (threshold){
                case 0:
                    checkNum = 10;
                    scheduledTime = 500;
                case 1:
                    checkNum = 20;
                    scheduledTime = 1000;
                case 2:
                    checkNum = 30;
                    scheduledTime = 1500;
                default:
                    checkNum = 40;
                    scheduledTime = 2000;
            }
            //每间隔1s 随机取样十个sessionId放入阻塞队列
            if (sessionIdSetArray.length > 0){
                for (int i = 0; i < checkNum; i++) {
                    //在数组索引范围内随机生成整数，这些随机数可以作为我们后续的索引值
                    int temp = random.nextInt(sessionIdSetArray.length);
                    //基于随机的索引值，获取到对应位置的SessionId
                    Integer sessionIdStr = sessionIdSetArray[temp];
                    //把SessionId放入阻塞队列
                    this.blockingQueue.put(sessionIdStr);
                }
            }
            Thread.sleep(scheduledTime);
        }
    }

    public void doConsume() throws InterruptedException {
        Integer sessionId;
        long currentTime;
        while (this.flag){
            currentTime = System.currentTimeMillis();
            sessionId = this.blockingQueue.take();
            TomcatHttpSession httpSession = (TomcatHttpSession) SessionManager.sessionContainer.get(sessionId);
            if (currentTime - httpSession.getCreateTime() >=httpSession.getTtl() && !httpSession.getTtlMark()){
                System.out.println(httpSession + "被标记为过期");
                httpSession.setTtlMark(true);
                httpSession.getSessionListener().destroyed(httpSession.getHttpSessionEvent());
            }
        }
    }

}
