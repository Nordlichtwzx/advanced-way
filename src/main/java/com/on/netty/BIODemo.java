package com.on.netty;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Nordlicht
 */
public class BIODemo {

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");
        while (true) {
            System.out.println("等待连接");
            final Socket client = serverSocket.accept();
            System.out.println("连接上了");
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                    System.out.println("一个客户端连接");
                    handle(client);
                }
            });
        }
    }

    public static void handle(Socket client) {
        byte[] message = new byte[1024];
        try {
            InputStream inputStream = client.getInputStream();
            while (true) {
                System.out.println("等待读取");
                int read = inputStream.read(message);
                if (read != -1) {
                    System.out.println(new String(message));
                } else {
                    System.out.println("读完了");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<List<Integer>> threeSum(int[] nums) {
        if (nums == null || nums.length <= 2) {
            return null;
        }
        Arrays.sort(nums);
        Set<List<Integer>> set = new HashSet();
        for (int i = 0; i < nums.length - 2; i++) {
            int pre = i + 1;
            int tail = nums.length - 1;
            while (pre != tail) {
                int sum = nums[i] + nums[pre] + nums[tail];
                if (sum > 0) {
                    tail--;
                }
                if (sum < 0) {
                    pre++;
                }
                if (sum == 0) {
                    List<Integer> list = Arrays.asList(nums[i], nums[pre], nums[tail]);
                    set.add(list);
                }
            }
        }
        return new ArrayList<>(set);
    }

    public int minSubArrayLen(int s, int[] nums) {
        int len = nums.length;
        if (len == 0) {
            return 0;
        }
        int start = 0;
        int end = 0;
        int count = Integer.MAX_VALUE;
        int sum = 0;
        while (end < len) {
            sum += nums[end];
            while (sum >= s) {
                count = Math.min(end - start + 1, count);
                sum -= sum - nums[start];
                start++;
            }
            end++;
        }
        return count == Integer.MAX_VALUE ? 0 : count;
    }

    public int findKthLargest(int[] nums, int k) {
        Arrays.sort(nums);
        Set integers = new TreeSet(Arrays.asList(nums));
        return 0;
    }
}
