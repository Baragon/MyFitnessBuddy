package net.baragon.MyFitnessBuddy.client;

import net.baragon.MyFitnessBuddy.AddFoodActivity;
import net.baragon.MyFitnessBuddy.DiaryActivity;
import net.baragon.MyFitnessBuddy.util.FoodEntry;
import net.baragon.MyFitnessBuddy.util.FoodInfo;
import net.baragon.MyFitnessBuddy.util.Macros;
import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;


public class Client implements OnTaskFinishListener {
    public LoadingTaskExecutor executor;
    List<ALoadingTask> queue;

    public Client(LoadingTaskExecutor executor) {
        this.executor = executor;
        queue = new LinkedList<ALoadingTask>();
    }

    public void DeleteFood(DiaryActivity activity, int id) {
        ALoadingTask task = new DeleteEntryTask(activity, executor, this);
        queue.add(task);
        task.execute(new Integer[]{id});
    }


    public void LoadRecentFoods(AddFoodActivity activity) {
        ALoadingTask task = new RecentFoodTask(activity, executor, this);
        queue.add(task);
        task.execute();
    }


    public void LoadMeals(DiaryActivity activity, DateTime date) {
        ALoadingTask task = new GetMealsTask(activity, executor, this);
        queue.add(task);
        task.execute(new DateTime[]{date});
    }


    public void LogFood(DiaryActivity activity, FoodEntry entry, int meal, boolean isNewServing) {
        ALoadingTask task = new LogFoodTask(activity, executor, this);
        queue.add(task);
        task.execute(new Object[]{entry, meal, isNewServing});
    }

    public void NewFood(AddFoodActivity activity, FoodInfo info) {
        ALoadingTask task = new NewFoodTask(activity, executor, this);
        queue.add(task);
        task.execute(new FoodInfo[]{info});
    }


    public void NewMeal(DiaryActivity activity, String name, DateTime dateTime) {
        ALoadingTask task = new NewMealTask(activity, executor, this);
        queue.add(task);
        task.execute(new Object[]{name, dateTime});
    }

    public void FindFood(AddFoodActivity activity, String name) {
        FindFoodTask task = new FindFoodTask(activity, executor, this);
        queue.add(task);
        task.execute(new String[]{name});
    }

    public void SetGoals(DiaryActivity activity, Macros goals, DateTime date) {
        SetGoalsTask task = new SetGoalsTask(activity, executor, this);
        queue.add(task);
        task.execute(new Object[]{goals, date});
    }


    public void Init() {
        executor.Init();
    }

    public void DeleteLocalData() {
        executor.DeleteLocalData();
    }

    public void onTaskFinish(ALoadingTask loadingTask) {
        queue.remove(loadingTask);
    }


}
