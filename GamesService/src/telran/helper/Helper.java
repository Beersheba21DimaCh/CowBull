package telran.helper;

import java.io.*;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;


import telran.cowbull.utils.CowBullUtils;

public class Helper {


	public static void main(String[] args) throws IOException {
		String PATHWINNERS_SINGLE_GAME = "./winners/single";
		String path = PATHWINNERS_SINGLE_GAME;
		
		File file = new File(path);
		System.out.println(file.exists());
		if (!file.exists())
			file.mkdirs();
		System.out.println(file.exists());
		

		
		
	}
//	private static void canselet(ScheduledExecutorService ex) {
//		Instant tStart = Instant.now();
//		while(true) {
//			if(Instant.now().isAfter(tStart.plusSeconds(20))) {
//				System.out.println("Instant.now().isAfter(tStart.plusSeconds(10))");
//				
//				try {
//					ex.shutdown();
//					ex.awaitTermination(5, TimeUnit.SECONDS);
//					ex.shutdownNow();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				break;
//			}
//		}
//		
//	}
//	private static void fun1() {
//		System.out.println("call fun1");
//		while(true) {
//			System.out.println("fun1");
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				System.out.println("interrupt fun1");
//				break;
//			}
//		}
//	}
//	
//	private static void timer() {
//		System.out.println(Instant.now());
//	}
//	
//	private static Callable<String> task1(String str) {
//		return () -> str;
//		
//	}
//	
}
		
		
		






		
//		ExecutorService ex = Executors.newFixedThreadPool(2);
//		ex.submit(arrFun[0]);
		
//		for (int i = 0; i < 10000; i++) {
//			try {
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println(Thread.currentThread().getName() + " main");
//			
//		}
		

	
//	private void mymain() {
//		Runnable[] arr = {
//				this.fun1();
//		};
//	}
//	
//	private void fun1() {
//		
//	}
//	
//	private static void f1() {
//		
//		for (int i = 0; i < 10000; i++) {
//			try {
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println(Thread.currentThread().getName() + " f1");
//			
//		}
//
//	}
//	private static String getRnd() {
//		 		return		 new Random()
//				.ints(0, 9)
//				.distinct()
//				.limit(4)
//				.boxed()
//				.map(p -> String.valueOf(p))
//				.collect(Collectors.joining());
//	}
