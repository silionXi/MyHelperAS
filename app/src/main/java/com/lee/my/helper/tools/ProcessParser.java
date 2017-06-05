package com.lee.my.helper.tools;

public class ProcessParser {

	/** ����(�������������̣����߳�)�� */
	public static final int pid = 0;
	/** Ӧ�ó������������� */
	public static final int comm = 1;
	/**
	 * �����״̬��R:runnign, S:sleeping (TASK_INTERRUPTIBLE), D:disk sleep
	 * (TASK_UNINTERRUPTIBLE), T: stopped, T:tracing stop,Z:zombie, X:dead
	 */
	public static final int task_state = 2;
	/** ������ID */
	public static final int ppid = 3;
	/** �߳���� */
	public static final int pgid = 4;
	/** c���������ڵĻỰ��ID */
	public static final int sid = 5;
	/** �������tty�ն˵��豸�ţ�INT��34817/256��=���豸�ţ���34817-���豸�ţ�=���豸�� */
	public static final int tty_nr = 6;
	/** �ն˵Ľ�����ţ���ǰ�����ڸ����������ն˵�ǰ̨����(����shell Ӧ�ó���)��PID�� */
	public static final int tty_pgrp = 7;
	/** ���̱�־λ���鿴����������� */
	public static final int task_flags = 8;
	/** ��������Ҫ��Ӳ�̿����ݶ�������ȱҳ����ȱҳ���Ĵ��� */
	public static final int min_flt = 9;
	/** �ۼƵĸ���������е�waited-for�������������Ĵ�ȱҳ�Ĵ���Ŀ */
	public static final int cmin_flt = 10;
	/** ��������Ҫ��Ӳ�̿����ݶ�������ȱҳ����ȱҳ���Ĵ��� */
	public static final int maj_flt = 11;
	/** �ۼƵĸ���������е�waited-for����������������ȱҳ�Ĵ���Ŀ */
	public static final int cmaj_flt = 12;
	/** ���������û�̬���е�ʱ�䣬��λΪjiffies */
	public static final int utime = 13;
	/** �������ں���̬���е�ʱ�䣬��λΪjiffies */
	public static final int stime = 14;
	/** �ۼƵĸ���������е�waited-for�����������û�̬���е�ʱ�䣬��λΪjiffies */
	public static final int cutime = 15;
	/** �ۼƵĸ���������е�waited-for���������ں���̬���е�ʱ�䣬��λΪjiffies */
	public static final int cstime = 16;
	/** ����Ķ�̬���ȼ� */
	public static final int priority = 17;
	/** ����ľ�̬���ȼ� */
	public static final int nice = 18;
	/** ���������ڵ��߳������̵߳ĸ��� */
	public static final int num_threads = 19;
	/** ���ڼ�ʱ������µ���һ�� SIGALRM ���ͽ��̵�ʱ�ӣ��� jiffy Ϊ��λ. */
	public static final int it_real_value = 20;
	/** ������������ʱ�䣬��λΪjiffies */
	public static final int start_time = 21;
	/** ������������ַ�ռ��С */
	public static final int vsize = 22;
	/** ������ǰפ�������ַ�ռ�Ĵ�С */
	public static final int rss = 23;
	/** ��������פ�������ַ�ռ�����ֵ */
	public static final int rlim = 24;
	/** �������������ַ�ռ�Ĵ���ε���ʼ��ַ */
	public static final int start_code = 25;
	/** �������������ַ�ռ�Ĵ���εĽ�����ַ */
	public static final int end_code = 26;
	/** �������������ַ�ռ��ջ�Ľ�����ַ */
	public static final int start_stack = 27;
	/** esp(32 λ��ջָ��) �ĵ�ǰֵ, ���ڽ��̵��ں˶�ջҳ�õ���һ��. */
	public static final int kstkesp = 28;
	/** ָ��Ҫִ�е�ָ���ָ��, EIP(32 λָ��ָ��)�ĵ�ǰֵ. */
	public static final int kstkeip = 29;
	/** �������źŵ�λͼ����¼���͸����̵���ͨ�ź� */
	public static final int pendingsig = 30;
	/** �����źŵ�λͼ */
	public static final int block_sig = 31;
	/** ���Ե��źŵ�λͼ */
	public static final int sigign = 32;
	/** ��������źŵ�λͼ */
	public static final int sigcatch = 33;
	/** ����ý�����˯��״̬����ֵ�������ȵĵ��õ� */
	public static final int wchan = 34;
	/** �ý��̽���ʱ���򸸽��������͵��ź� */
	public static final int exit_signal = 35;
	/** �������ĸ�CPU�� */
	public static final int task_cpu = 36;
	/** ʵʱ���̵�������ȼ��� */
	public static final int task_rt_priority = 37;
	/** ���̵ĵ��Ȳ��ԣ�0=��ʵʱ���̣�1=FIFOʵʱ���̣�2=RRʵʱ���� */
	public static final int task_policy = 38;

	private String[] DATA;

	public ProcessParser(String infos) {
		// TODO Auto-generated constructor stub
		DATA = infos.split(" ");
	}

	/** ����(�������������̣����߳�)�� */
	public String get_pid() {
		return DATA[pid];
	}

	/** Ӧ�ó������������� */
	public String get_comm() {
		return DATA[comm];
	}

	/**
	 * �����״̬��R:runnign, S:sleeping (TASK_INTERRUPTIBLE), D:disk sleep
	 * (TASK_UNINTERRUPTIBLE), T: stopped, T:tracing stop,Z:zombie, X:dead
	 */
	public String get_task_state() {
		return DATA[task_state];
	}

	/** ������ID */
	public String get_ppid() {
		return DATA[ppid];
	}

	/** �߳���� */
	public String get_pgid() {
		return DATA[pgid];
	}

	/** c���������ڵĻỰ��ID */
	public String get_sid() {
		return DATA[sid];
	}

	/** �������tty�ն˵��豸�ţ�INT��34817/256��=���豸�ţ���34817-���豸�ţ�=���豸�� */
	public String get_tty_nr() {
		return DATA[tty_nr];
	}

	/** �ն˵Ľ�����ţ���ǰ�����ڸ����������ն˵�ǰ̨����(����shell Ӧ�ó���)��PID�� */
	public String get_tty_pgrp() {
		return DATA[tty_pgrp];
	}

	/** ���̱�־λ���鿴����������� */
	public String get_task_flags() {
		return DATA[task_flags];
	}

	/** ��������Ҫ��Ӳ�̿����ݶ�������ȱҳ����ȱҳ���Ĵ��� */
	public String get_min_flt() {
		return DATA[min_flt];
	}

	/** �ۼƵĸ���������е�waited-for�������������Ĵ�ȱҳ�Ĵ���Ŀ */
	public String get_cmin_flt() {
		return DATA[cmin_flt];
	}

	/** ��������Ҫ��Ӳ�̿����ݶ�������ȱҳ����ȱҳ���Ĵ��� */
	public String get_maj_flt() {
		return DATA[maj_flt];
	}

	/** �ۼƵĸ���������е�waited-for����������������ȱҳ�Ĵ���Ŀ */
	public String get_cmaj_flt() {
		return DATA[cmaj_flt];
	}

	/** ���������û�̬���е�ʱ�䣬��λΪjiffies */
	public String get_utime() {
		return DATA[utime];
	}

	/** �������ں���̬���е�ʱ�䣬��λΪjiffies */
	public String get_stime() {
		return DATA[stime];
	}

	/** �ۼƵĸ���������е�waited-for�����������û�̬���е�ʱ�䣬��λΪjiffies */
	public String get_cutime() {
		return DATA[cutime];
	}

	/** �ۼƵĸ���������е�waited-for���������ں���̬���е�ʱ�䣬��λΪjiffies */
	public String get_cstime() {
		return DATA[cstime];
	}

	/** ����Ķ�̬���ȼ� */
	public String get_priority() {
		return DATA[priority];
	}

	/** ����ľ�̬���ȼ� */
	public String get_nice() {
		return DATA[nice];
	}

	/** ���������ڵ��߳������̵߳ĸ��� */
	public String get_num_threads() {
		return DATA[num_threads];
	}

	/** ���ڼ�ʱ������µ���һ�� SIGALRM ���ͽ��̵�ʱ�ӣ��� jiffy Ϊ��λ. */
	public String get_it_real_value() {
		return DATA[it_real_value];
	}

	/** ������������ʱ�䣬��λΪjiffies */
	public String get_start_time() {
		return DATA[start_time];
	}

	/** ������������ַ�ռ��С */
	public String get_vsize() {
		int rss = Integer.parseInt(get_rss()) / 1024;
		return Integer.parseInt(DATA[vsize]) / 1024 / 1024 / 8 + rss + "MB";
	}

	/** ������ǰפ�������ַ�ռ�Ĵ�С */
	public String get_rss() {
		return DATA[rss];
	}

	/** ��������פ�������ַ�ռ�����ֵ */
	public String get_rlim() {
		return DATA[rlim];
	}

	/** �������������ַ�ռ�Ĵ���ε���ʼ��ַ */
	public String get_start_code() {
		return DATA[start_code];
	}

	/** �������������ַ�ռ�Ĵ���εĽ�����ַ */
	public String get_end_code() {
		return DATA[end_code];
	}

	/** �������������ַ�ռ��ջ�Ľ�����ַ */
	public String get_start_stack() {
		return DATA[start_stack];
	}

	/** esp(32 λ��ջָ��) �ĵ�ǰֵ, ���ڽ��̵��ں˶�ջҳ�õ���һ��. */
	public String get_kstkesp() {
		return DATA[kstkesp];
	}

	/** ָ��Ҫִ�е�ָ���ָ��, EIP(32 λָ��ָ��)�ĵ�ǰֵ. */
	public String get_kstkeip() {
		return DATA[kstkeip];
	}

	/** �������źŵ�λͼ����¼���͸����̵���ͨ�ź� */
	public String get_pendingsig() {
		return DATA[pendingsig];
	}

	/** �����źŵ�λͼ */
	public String get_block_sig() {
		return DATA[block_sig];
	}

	/** ���Ե��źŵ�λͼ */
	public String get_sigign() {
		return DATA[sigign];
	}

	/** ��������źŵ�λͼ */
	public String get_sigcatch() {
		return DATA[sigcatch];
	}

	/** ����ý�����˯��״̬����ֵ�������ȵĵ��õ� */
	public String get_wchan() {
		return DATA[wchan];
	}

	/** �ý��̽���ʱ���򸸽��������͵��ź� */
	public String get_exit_signal() {
		return DATA[exit_signal];
	}

	/** �������ĸ�CPU�� */
	public String get_task_cpu() {
		return DATA[task_cpu];
	}

	/** ʵʱ���̵�������ȼ��� */
	public String get_task_rt_priority() {
		return DATA[task_rt_priority];
	}

	/** ���̵ĵ��Ȳ��ԣ�0=��ʵʱ���̣�1=FIFOʵʱ���̣�2=RRʵʱ���� */
	public String get_task_policy() {
		return DATA[task_policy];
	}

}
