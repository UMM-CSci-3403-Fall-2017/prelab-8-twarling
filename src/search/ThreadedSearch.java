package search;

import java.util.ArrayList;

public class ThreadedSearch<T> implements Runnable {

  private T target;
  private ArrayList<T> list;
  private int begin;
  private int end;
  private Answer answer;

  public ThreadedSearch() {
  }

  private ThreadedSearch(T target, ArrayList<T> list, int begin, int end, Answer answer) {
    this.target=target;
    this.list=list;
    this.begin=begin;
    this.end=end;
    this.answer=answer;
  }

  /**
  * Searches `list` in parallel using `numThreads` threads.
  *
  * You can assume that the list size is divisible by `numThreads`
  */
  public boolean parSearch(int numThreads, T target, ArrayList<T> list) throws InterruptedException {
	
	Answer answer = new Answer();
	int subDiv = list.size() / numThreads;
	
	// Creates a thread array that all of the threads will be stored in
	Thread[] threads = new Thread[numThreads];
	
	//Creates the specified number of threads in the thread array, and creates a new instance of ThreadedSearch for each individual thread
	for(int i = 0; i < numThreads; i++){
		threads[i] = new Thread (new ThreadedSearch(target, list, subDiv * i, subDiv * (i + 1), answer));
		threads[i].start();
	}
	
	// forces the program to wait for all of the threads to finish searching before moving on
	for(int i = 0; i < numThreads; i++){
		threads[i].join();
	}
	
	
    return answer.getAnswer();
  }

  public void run() {
	  
	  // Loops through the list within the given beginning and end locations, and checks if the value at i is equal to the number being looked for.
	  // If the value at i is equal to the one being looked for, answer is set to true and the loop is ended. 
	  for(int i = begin; i < end; i++){
          if(list.get(i).equals(target)) {
        	  answer.setAnswer(true);
        	  return;
          }
      }

  }

  private class Answer {
    private boolean answer = false;

    public boolean getAnswer() {
      return answer;
    }

    // This has to be synchronized to ensure that no two threads modify
    // this at the same time, possibly causing race conditions.
    public synchronized void setAnswer(boolean newAnswer) {
      answer = newAnswer;
    }
  }

}

