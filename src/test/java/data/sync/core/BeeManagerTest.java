package data.sync.core;

import data.sync.common.ClusterMessages;
import org.junit.Test;
import scala.Tuple3;

import java.util.Date;

/**
 * Created by hesiyuan on 15/6/25.
 */
public class BeeManagerTest {
    @Test
    public void test1(){
        ClusterMessages.TaskInfo t1 = new ClusterMessages.TaskInfo("t1","test-job1","","","","","","","","",TaskStatus.STARTED);
        ClusterMessages.TaskInfo t2 = new ClusterMessages.TaskInfo("t2","test-job1","","","","","","","","",TaskStatus.STARTED);
        ClusterMessages.TaskInfo t3 = new ClusterMessages.TaskInfo("t3","test-job1","","","","","","","","",TaskStatus.STARTED);
        ClusterMessages.TaskInfo t4 = new ClusterMessages.TaskInfo("t4","test-job1","","","","","","","","",TaskStatus.STARTED);
        ClusterMessages.TaskInfo t5 = new ClusterMessages.TaskInfo("t5","test-job1","","","","","","","","",TaskStatus.STARTED);
        ClusterMessages.TaskInfo t6 = new ClusterMessages.TaskInfo("t6","test-job1","","","","","","","","",TaskStatus.STARTED);
        ClusterMessages.TaskInfo t7 = new ClusterMessages.TaskInfo("t7","test-job1","","","","","","","","",TaskStatus.STARTED);
        ClusterMessages.TaskInfo t8 = new ClusterMessages.TaskInfo("t8","test-job1","","","","","","","","",TaskStatus.STARTED);
        scala.collection.mutable.HashSet<ClusterMessages.TaskInfo> tasks = new scala.collection.mutable.HashSet<ClusterMessages.TaskInfo>();
        tasks.add(t1);
        tasks.add(t2);
        tasks.add(t3);
        tasks.add(t4);
        tasks.add(t5);
        tasks.add(t6);
        tasks.add(t7);
        tasks.add(t8);



        JobInfo job1 = new JobInfo("test-job1",
                1,
                new Date().getTime(),
                "",
                null,
                tasks,
                new scala.collection.mutable.HashSet<ClusterMessages.TaskInfo>(),
                new scala.collection.mutable.HashSet<ClusterMessages.TaskInfo>(),
                JobStatus.SUBMITED
        );

        JobInfo job2 = new JobInfo("test-job2",
                1,
                new Date().getTime()-10000,
                "",
                null,
                new scala.collection.mutable.HashSet<ClusterMessages.TaskInfo>(),
                new scala.collection.mutable.HashSet<ClusterMessages.TaskInfo>(),
                new scala.collection.mutable.HashSet<ClusterMessages.TaskInfo>(),
                JobStatus.SUBMITED
        );

//        FIFOScheduler.addJob(job2);
        FIFOScheduler.addJob(job1);

        JobManager.addJob(job1);

//        for(Tuple3 t :FIFOScheduler.queue()){
//            System.out.println(t._1());
//        }

        BeeManager.connDic().put("bee1",new BeeDesc(5,10,"bee1",null));
        BeeManager.connDic().put("bee2",new BeeDesc(4,10,"bee2",null));
        BeeManager.connDic().put("bee3", new BeeDesc(8, 10, "bee3", null));
//        System.out.println(BeeManager.getMostFreeBee());

        Object[] assigns = FIFOScheduler.assigns();
        for(int i=0;i<assigns.length;i++){
            System.out.println(assigns[i]);
        }
        JobManager.printMem();
    }
}
