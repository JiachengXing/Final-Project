package com.example.project;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.base.BaseActivity;
import com.example.project.fragment.HomeFragment;
import com.example.project.fragment.MineFragment;
import com.example.project.fragment.ProjectFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private int mCurrentPage = -1;

    public static final int FRAGMENT_HOME = 1;
    public static final int FRAGMENT_PROJECT = 2;
    public static final int FRAGMENT_MINE = 3;

    private FragmentManager mFragmentManager;
    private SparseArray<Fragment> mFragmentMap;

    private Fragment mHomeFragment, mProjectFragment,mMineFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        mFragmentMap = new SparseArray<>();
        pageTo(FRAGMENT_HOME);
        ((BottomNavigationView)findViewById(R.id.bottomnavigation)).setOnNavigationItemSelectedListener(this);


    }

    public void pageTo(int pageIndex) {
        mCurrentPage = pageIndex;
        if (mCurrentPage == FRAGMENT_HOME) {
            ((BottomNavigationView)findViewById(R.id.bottomnavigation)).setSelectedItemId(R.id.menu_home);
        } else if (mCurrentPage == FRAGMENT_PROJECT) {
            ((BottomNavigationView)findViewById(R.id.bottomnavigation)).setSelectedItemId(R.id.menu_project);
        } else if (mCurrentPage == FRAGMENT_MINE) {
            ((BottomNavigationView)findViewById(R.id.bottomnavigation)).setSelectedItemId(R.id.menu_mine);
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragment(transaction);
        showFragment(pageIndex, transaction, mFragmentMap.get(pageIndex));
    }

    /**
     * 隐藏
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }

        if (mProjectFragment != null) {
            transaction.hide(mProjectFragment);
        }

        if (mMineFragment != null) {
            transaction.hide(mMineFragment);
        }
    }

    /**
     * 显示
     */
    private void showFragment(int index, FragmentTransaction transaction, Fragment fragment) {
        if (fragment == null) {
            // 当传入的fragment没有被初始化
            if (index == FRAGMENT_HOME) {
                fragment = mHomeFragment = HomeFragment.newInstance("sharing","sharing");
            } else if (index == FRAGMENT_PROJECT) {
                fragment = mProjectFragment = ProjectFragment.newInstance("plan","plan");
            } else {
                fragment = mMineFragment = MineFragment.newInstance("mine","mine");
            }
            transaction.add(R.id.framelayout, fragment, fragment.getClass().getSimpleName());
            // 缓存住已经初始化的fragment，以便点击tab时传入到此方法中。
            mFragmentMap.put(index, fragment);
        } else {
            transaction.show(fragment);
        }
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_home) {
            if (mCurrentPage != FRAGMENT_HOME) {
                pageTo(FRAGMENT_HOME);
            }
            return true;
        } else if (itemId == R.id.menu_project) {
            if (mCurrentPage != FRAGMENT_PROJECT) {
                pageTo(FRAGMENT_PROJECT);
            }
            return true;
        } else if (itemId == R.id.menu_mine) {
            if (mCurrentPage != FRAGMENT_MINE) {
                pageTo(FRAGMENT_MINE);
            }
            return true;
        }
        return false;
    }

    @Override
    protected int setStatusBarColor() {
        return R.color.windowBackground;
    }
}