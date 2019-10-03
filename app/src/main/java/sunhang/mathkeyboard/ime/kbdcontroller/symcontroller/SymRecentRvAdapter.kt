package sunhang.mathkeyboard.ime.kbdcontroller.symcontroller

import android.annotation.SuppressLint
import sunhang.mathkeyboard.ime.IMSContext
import androidx.recyclerview.widget.DiffUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import sunhang.mathkeyboard.data.SymEntity


class SymRecentRvAdapter private constructor(imsContext: IMSContext) :
    SymRvAdapter(imsContext, listOf()) {

    companion object {
        @SuppressLint("CheckResult")
        fun build(imsContext: IMSContext): SymRecentRvAdapter {
            val adapter = SymRecentRvAdapter(imsContext)

            // todo 可能会有内存泄漏
            imsContext.kbdDb.recentDao().getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list: List<SymEntity> ->
                val oldList = adapter.list
                val newList = list.map { it.content }

                val diffResult = DiffUtil.calculateDiff(DiffCallBack(oldList, newList), true)
                adapter.list = newList
                diffResult.dispatchUpdatesTo(adapter)
            }, {
                it.printStackTrace()
            })

            return adapter
        }
    }

    class DiffCallBack(
        private val oldDatas: List<String>,
        private val newDatas: List<String>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldDatas.size
        }

        override fun getNewListSize(): Int {
            return newDatas.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

            return oldDatas[oldItemPosition] == newDatas[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldDatas[oldItemPosition] == newDatas[newItemPosition]
        }
    }
}