package org.techtown.archivebook;

public abstract class ThreadTask<T1, T2> implements Runnable {
    T1 argument;
    T2 result;

    final public void execute(final T1 arg) {
        argument = arg;
        Thread thread = new Thread(this);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            onPostExecute(null);
            return;
        }
        onPostExecute(result);
    }

    @Override
    public void run() {
        result = doInBackgraound(argument);
    }

    protected abstract T2 doInBackgraound(T1 argument);

    protected abstract void onPostExecute(T2 result);
}
