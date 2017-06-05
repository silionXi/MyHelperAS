package com.lee.my.helper.tools;

public class ProcessParser {

	/** 进程(包括轻量级进程，即线程)号 */
	public static final int pid = 0;
	/** 应用程序或命令的名字 */
	public static final int comm = 1;
	/**
	 * 任务的状态，R:runnign, S:sleeping (TASK_INTERRUPTIBLE), D:disk sleep
	 * (TASK_UNINTERRUPTIBLE), T: stopped, T:tracing stop,Z:zombie, X:dead
	 */
	public static final int task_state = 2;
	/** 父进程ID */
	public static final int ppid = 3;
	/** 线程组号 */
	public static final int pgid = 4;
	/** c该任务所在的会话组ID */
	public static final int sid = 5;
	/** 该任务的tty终端的设备号，INT（34817/256）=主设备号，（34817-主设备号）=次设备号 */
	public static final int tty_nr = 6;
	/** 终端的进程组号，当前运行在该任务所在终端的前台任务(包括shell 应用程序)的PID。 */
	public static final int tty_pgrp = 7;
	/** 进程标志位，查看该任务的特性 */
	public static final int task_flags = 8;
	/** 该任务不需要从硬盘拷数据而发生的缺页（次缺页）的次数 */
	public static final int min_flt = 9;
	/** 累计的该任务的所有的waited-for进程曾经发生的次缺页的次数目 */
	public static final int cmin_flt = 10;
	/** 该任务需要从硬盘拷数据而发生的缺页（主缺页）的次数 */
	public static final int maj_flt = 11;
	/** 累计的该任务的所有的waited-for进程曾经发生的主缺页的次数目 */
	public static final int cmaj_flt = 12;
	/** 该任务在用户态运行的时间，单位为jiffies */
	public static final int utime = 13;
	/** 该任务在核心态运行的时间，单位为jiffies */
	public static final int stime = 14;
	/** 累计的该任务的所有的waited-for进程曾经在用户态运行的时间，单位为jiffies */
	public static final int cutime = 15;
	/** 累计的该任务的所有的waited-for进程曾经在核心态运行的时间，单位为jiffies */
	public static final int cstime = 16;
	/** 任务的动态优先级 */
	public static final int priority = 17;
	/** 任务的静态优先级 */
	public static final int nice = 18;
	/** 该任务所在的线程组里线程的个数 */
	public static final int num_threads = 19;
	/** 由于计时间隔导致的下一个 SIGALRM 发送进程的时延，以 jiffy 为单位. */
	public static final int it_real_value = 20;
	/** 该任务启动的时间，单位为jiffies */
	public static final int start_time = 21;
	/** 该任务的虚拟地址空间大小 */
	public static final int vsize = 22;
	/** 该任务当前驻留物理地址空间的大小 */
	public static final int rss = 23;
	/** 该任务能驻留物理地址空间的最大值 */
	public static final int rlim = 24;
	/** 该任务在虚拟地址空间的代码段的起始地址 */
	public static final int start_code = 25;
	/** 该任务在虚拟地址空间的代码段的结束地址 */
	public static final int end_code = 26;
	/** 该任务在虚拟地址空间的栈的结束地址 */
	public static final int start_stack = 27;
	/** esp(32 位堆栈指针) 的当前值, 与在进程的内核堆栈页得到的一致. */
	public static final int kstkesp = 28;
	/** 指向将要执行的指令的指针, EIP(32 位指令指针)的当前值. */
	public static final int kstkeip = 29;
	/** 待处理信号的位图，记录发送给进程的普通信号 */
	public static final int pendingsig = 30;
	/** 阻塞信号的位图 */
	public static final int block_sig = 31;
	/** 忽略的信号的位图 */
	public static final int sigign = 32;
	/** 被俘获的信号的位图 */
	public static final int sigcatch = 33;
	/** 如果该进程是睡眠状态，该值给出调度的调用点 */
	public static final int wchan = 34;
	/** 该进程结束时，向父进程所发送的信号 */
	public static final int exit_signal = 35;
	/** 运行在哪个CPU上 */
	public static final int task_cpu = 36;
	/** 实时进程的相对优先级别 */
	public static final int task_rt_priority = 37;
	/** 进程的调度策略，0=非实时进程，1=FIFO实时进程；2=RR实时进程 */
	public static final int task_policy = 38;

	private String[] DATA;

	public ProcessParser(String infos) {
		// TODO Auto-generated constructor stub
		DATA = infos.split(" ");
	}

	/** 进程(包括轻量级进程，即线程)号 */
	public String get_pid() {
		return DATA[pid];
	}

	/** 应用程序或命令的名字 */
	public String get_comm() {
		return DATA[comm];
	}

	/**
	 * 任务的状态，R:runnign, S:sleeping (TASK_INTERRUPTIBLE), D:disk sleep
	 * (TASK_UNINTERRUPTIBLE), T: stopped, T:tracing stop,Z:zombie, X:dead
	 */
	public String get_task_state() {
		return DATA[task_state];
	}

	/** 父进程ID */
	public String get_ppid() {
		return DATA[ppid];
	}

	/** 线程组号 */
	public String get_pgid() {
		return DATA[pgid];
	}

	/** c该任务所在的会话组ID */
	public String get_sid() {
		return DATA[sid];
	}

	/** 该任务的tty终端的设备号，INT（34817/256）=主设备号，（34817-主设备号）=次设备号 */
	public String get_tty_nr() {
		return DATA[tty_nr];
	}

	/** 终端的进程组号，当前运行在该任务所在终端的前台任务(包括shell 应用程序)的PID。 */
	public String get_tty_pgrp() {
		return DATA[tty_pgrp];
	}

	/** 进程标志位，查看该任务的特性 */
	public String get_task_flags() {
		return DATA[task_flags];
	}

	/** 该任务不需要从硬盘拷数据而发生的缺页（次缺页）的次数 */
	public String get_min_flt() {
		return DATA[min_flt];
	}

	/** 累计的该任务的所有的waited-for进程曾经发生的次缺页的次数目 */
	public String get_cmin_flt() {
		return DATA[cmin_flt];
	}

	/** 该任务需要从硬盘拷数据而发生的缺页（主缺页）的次数 */
	public String get_maj_flt() {
		return DATA[maj_flt];
	}

	/** 累计的该任务的所有的waited-for进程曾经发生的主缺页的次数目 */
	public String get_cmaj_flt() {
		return DATA[cmaj_flt];
	}

	/** 该任务在用户态运行的时间，单位为jiffies */
	public String get_utime() {
		return DATA[utime];
	}

	/** 该任务在核心态运行的时间，单位为jiffies */
	public String get_stime() {
		return DATA[stime];
	}

	/** 累计的该任务的所有的waited-for进程曾经在用户态运行的时间，单位为jiffies */
	public String get_cutime() {
		return DATA[cutime];
	}

	/** 累计的该任务的所有的waited-for进程曾经在核心态运行的时间，单位为jiffies */
	public String get_cstime() {
		return DATA[cstime];
	}

	/** 任务的动态优先级 */
	public String get_priority() {
		return DATA[priority];
	}

	/** 任务的静态优先级 */
	public String get_nice() {
		return DATA[nice];
	}

	/** 该任务所在的线程组里线程的个数 */
	public String get_num_threads() {
		return DATA[num_threads];
	}

	/** 由于计时间隔导致的下一个 SIGALRM 发送进程的时延，以 jiffy 为单位. */
	public String get_it_real_value() {
		return DATA[it_real_value];
	}

	/** 该任务启动的时间，单位为jiffies */
	public String get_start_time() {
		return DATA[start_time];
	}

	/** 该任务的虚拟地址空间大小 */
	public String get_vsize() {
		int rss = Integer.parseInt(get_rss()) / 1024;
		return Integer.parseInt(DATA[vsize]) / 1024 / 1024 / 8 + rss + "MB";
	}

	/** 该任务当前驻留物理地址空间的大小 */
	public String get_rss() {
		return DATA[rss];
	}

	/** 该任务能驻留物理地址空间的最大值 */
	public String get_rlim() {
		return DATA[rlim];
	}

	/** 该任务在虚拟地址空间的代码段的起始地址 */
	public String get_start_code() {
		return DATA[start_code];
	}

	/** 该任务在虚拟地址空间的代码段的结束地址 */
	public String get_end_code() {
		return DATA[end_code];
	}

	/** 该任务在虚拟地址空间的栈的结束地址 */
	public String get_start_stack() {
		return DATA[start_stack];
	}

	/** esp(32 位堆栈指针) 的当前值, 与在进程的内核堆栈页得到的一致. */
	public String get_kstkesp() {
		return DATA[kstkesp];
	}

	/** 指向将要执行的指令的指针, EIP(32 位指令指针)的当前值. */
	public String get_kstkeip() {
		return DATA[kstkeip];
	}

	/** 待处理信号的位图，记录发送给进程的普通信号 */
	public String get_pendingsig() {
		return DATA[pendingsig];
	}

	/** 阻塞信号的位图 */
	public String get_block_sig() {
		return DATA[block_sig];
	}

	/** 忽略的信号的位图 */
	public String get_sigign() {
		return DATA[sigign];
	}

	/** 被俘获的信号的位图 */
	public String get_sigcatch() {
		return DATA[sigcatch];
	}

	/** 如果该进程是睡眠状态，该值给出调度的调用点 */
	public String get_wchan() {
		return DATA[wchan];
	}

	/** 该进程结束时，向父进程所发送的信号 */
	public String get_exit_signal() {
		return DATA[exit_signal];
	}

	/** 运行在哪个CPU上 */
	public String get_task_cpu() {
		return DATA[task_cpu];
	}

	/** 实时进程的相对优先级别 */
	public String get_task_rt_priority() {
		return DATA[task_rt_priority];
	}

	/** 进程的调度策略，0=非实时进程，1=FIFO实时进程；2=RR实时进程 */
	public String get_task_policy() {
		return DATA[task_policy];
	}

}
