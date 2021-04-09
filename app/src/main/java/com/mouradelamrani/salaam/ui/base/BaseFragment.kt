package com.mouradelamrani.salaam.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


/** Base fragment class that can be attached by [BaseActivity] or another [BaseFragment]. */
abstract class BaseFragment : Fragment() {

    protected open val baseActivity: BaseActivity
        get() = requireActivity() as BaseActivity

    /** Fragment's layout id. */
    protected abstract val layoutId: Int


    // region lifecycle

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is BaseActivity) {
            throw RuntimeException("BaseFragment must be attached to BaseActivity")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModels()
        observeViewModels()
    }

    // endregion


    // region abstract

    /**
     * This method is called in [onViewCreated]. It may be overridden to initialize views
     * on Fragment creation.
     */
    protected open fun initViews() {}

    /**
     * This method is called in [onActivityCreated]. It must be used to initialize view models
     * from it's own view model factory or another providers such as provider of parent fragment or activity.
     */
    protected open fun initViewModels() {}

    /**
     * This method is called in [onActivityCreated] after [initViewModels]. It must be used to set up
     * view model observers.
     */
    protected open fun observeViewModels() {}

    // endregion


}