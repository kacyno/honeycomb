package data.sync.core.storage;



import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Storage {

	private int destructLimit = 0;

	public Thread sinkThread = null;

    public Line createLine(){
        return new DefaultLine();
    }

	public boolean init(String id, int lineLimit, int byteLimit,
			int destructLimit) {
		if (this.stat == null) {
			this.stat = new Statistics(id, this);
		}
		this.stat.periodPass();
		pushClosed = false;
		if (lineLimit <= 0 || byteLimit <= 0) {
			return false;
		}
		
		this.destructLimit = destructLimit;
		
		return true;
	}
	
    /**
     * Get desctruct limit value
     *
     * @return
     *      desctructLimit
     */
	public int getDestructLimit(){
		return destructLimit;
	}

	/**
	 * Get information about {@link Storage}.
	 * 
	 * @return
	 * 			{@link Storage} information.
	 * 
	 * */
	abstract public String info();


	abstract public boolean push(Line line);


	abstract public boolean push(Line[] lines, int size);

	/**
	 * For test.
	 * 
	 * */
	abstract public boolean fakePush(int lineLength);


	abstract public Line pull();

	abstract public int pull(Line[] lines);

	/**
	 * Check if {@link Storage} is closed for pushing data.
	 * 
	 * @return
	 * 			true for closed, false for not closed.
	 * 
	 * */
	public boolean isPushClosed() {
		return pushClosed;
	}

	/**
	 * Set push status.
	 * 
	 * @param 	close
	 * 			true for closing push, false for opening push.
	 * 
	 * */
	public void setPushClosed(boolean close) {
		pushClosed = close;
		return;
	}

	/**
	 * Get size of {@link Storage} in bytes
	 * 
	 * @return
	 * 			Storage size.
	 * 
	 * */
	abstract public int size();

	/**
	 * Check {@link Storage} is empty.
	 * 
	 * @return
	 * 			true if empty.
	 * 
	 * */
	abstract public boolean empty();

	abstract public int getLineLimit();

	public Statistics getStat() {
		return this.stat;
	}

	private Statistics stat;

	private boolean pushClosed;

	public class Statistics {
		private String id;

		private Date beginTime;

		private Date endTime;

		private long lineRx;

		private long lineTx;

		private long byteRx;

		private long byteTx;

		private long lineRRefused;

		private long lineTRefused;

		private long periodInSeconds;

		private long lineRxTotal;

		private long lineTxTotal;

		private long byteRxTotal;

		private long byteTxTotal;

		private long totalSeconds;

		private Storage storage;

		public Statistics(String id, Storage storage) {
			this.storage = storage;
			this.setId(id);
			lineRx = 0;
			lineTx = 0;
			byteRx = 0;
			byteTx = 0;
			lineRRefused = 0;
			lineTRefused = 0;
			lineRxTotal = 0;
			lineTxTotal = 0;
			byteRxTotal = 0;
			byteTxTotal = 0;
			totalSeconds = 0;
			beginTime = new Date();
		}

		public void periodPass() {
			lineRx = 0;
			lineTx = 0;
			byteRx = 0;
			byteTx = 0;
			// lineRRefused = 0;
			// lineTRefused = 0;
			totalSeconds += periodInSeconds;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public long getLineRx() {
			return lineRx;
		}

		public void incLineRx(long i) {
			this.lineRx += i;
			this.incLineRxTotal(i);
		}

		public long getLineTx() {
			return lineTx;
		}

		public void incLineTx(long i) {
			this.lineTx += i;
			this.incLineTxTotal(i);
		}

		public long getByteRx() {
			return byteRx;
		}

		public void incByteRx(long i) {
			this.byteRx += i;
			this.incByteRxTotal(i);
		}

		public long getByteTx() {
			return byteTx;
		}

		public void incByteTx(long i) {
			this.byteTx += i;
			this.incByteTxTotal(i);
		}

		public long getLineRRefused() {
			return lineRRefused;
		}

		public void incLineRRefused(long lineRRefused) {
			this.lineRRefused += lineRRefused;
		}

		public long getLineTRefused() {
			return lineTRefused;
		}

		public void incLineTRefused(long lineTRefused) {
			this.lineTRefused += lineTRefused;
		}

		public long getPeriodInSeconds() {
			return periodInSeconds;
		}

		public void setPeriodInSeconds(long periodInSeconds) {
			this.periodInSeconds = periodInSeconds;
		}

		public long getLineRxTotal() {
			return lineRxTotal;
		}

		public void incLineRxTotal(long lineRxTotal) {
			this.lineRxTotal += lineRxTotal;
		}

		public long getLineTxTotal() {
			return lineTxTotal;
		}

		public void incLineTxTotal(long lineTxTotal) {
			this.lineTxTotal += lineTxTotal;
		}

		public long getByteRxTotal() {
			return byteRxTotal;
		}

		public void incByteRxTotal(long byteRxTotal) {
			this.byteRxTotal += byteRxTotal;
		}

		public long getByteTxTotal() {
			return byteTxTotal;
		}

		public void incByteTxTotal(long byteTxTotal) {
			this.byteTxTotal += byteTxTotal;
		}

		public String getSpeed(long byteNum, long seconds) {
			if (seconds == 0) {
				seconds = 1;
			}
			long bytePerSecond = byteNum / seconds;
			long unit = bytePerSecond;
			if ((unit = bytePerSecond / 1000000) > 0) {
				return unit + "MB/s";
			} else if ((unit = bytePerSecond / 1000) > 0) {
				return unit + "KB/s";
			} else{
				if (byteNum > 0 && bytePerSecond <= 0) {
					bytePerSecond = 1;
				}
				return bytePerSecond + "B/s";
			}
		}

		/**
		 * Get average line speed
		 * @param	lines
		 * 			Line amount
		 * @param	seconds
		 * 			Costed time.
		 * @return
		 * 			Average line speed.
		 */
		public String getLineSpeed(long lines, long seconds) {
			if (seconds == 0) {
				seconds = 1;
			}
			long linePerSecond = lines / seconds;
			
			if (lines > 0 && linePerSecond <= 0) {
				linePerSecond = 1;
			}
			
			return linePerSecond + "L/s";
		}

		/**
		 * Get the state of storage space during a period
		 * @return
		 * 			String of State during a period. 
		 */
		public String getPeriodState() {
			return String.format("stat:  %s speed %s %s|", this.storage.info(),
					getSpeed(this.byteRx, this.periodInSeconds),
					getLineSpeed(this.lineRx, this.periodInSeconds));
		}

		/**
		 * Get all the state of storage space
		 * @return
		 * 			String of all the State. 
		 */
		public String getTotalStat() {
//			String lineCount = this.storage.info();
//			String[] lineCounts = this.storage.info().split(":");
//
//			long lineRx = Long.parseLong(lineCounts[0]);
//			long lineTx = Long.parseLong(lineCounts[1]);
//			endTime = new Date();
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			long timeElapsed = (endTime.getTime() - beginTime.getTime()) / 1000;
//			// statistic information for datax task
//			Reporter.stat
//					.put("READ_RECORDS", String.valueOf(lineRx).trim());
//			Reporter.stat.put("WRITE_RECORDS", String.valueOf(lineTx)
//					.trim());
//			Reporter.stat.put("BEGIN_TIME", df.format(beginTime).trim());
//			Reporter.stat.put("END_TIME", df.format(endTime).trim());
//			Reporter.stat.put("BYTE_RX_TOTAL",
//					String.valueOf(this.byteRxTotal).trim());
//
//			return String.format("\n"
//					+ "%-26s: %-18s\n"
//					+ "%-26s: %-18s\n"
//					+ "%-26s: %19s\n"
//					+ "%-26s: %19s\n"
//					+ "%-26s: %19s\n"
//					+ "%-26s: %19s\n" ,
//					 "DataX starts work at", df.format(beginTime),
//					 "DataX ends work at", df.format(endTime),
//					 "Total time costs",  String.valueOf(timeElapsed) + "s",
//					 "Average byte speed", getSpeed(this.byteRxTotal, timeElapsed),
//					 "Average line speed", getLineSpeed(lineRx, timeElapsed),
//					 "Total transferred records", String.valueOf(lineRx));
			return null;
		}
	}
}
