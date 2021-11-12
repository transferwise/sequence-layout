package com.transferwise.sequencelayout

/**
 * Adapter to bind data to [com.transferwise.sequencelayout.SequenceStep]s for a [com.transferwise.sequencelayout.SequenceLayout].
 */
public abstract class SequenceAdapter<T> where T : Any {

    abstract fun getCount(): Int

    abstract fun getItem(position: Int): T

    abstract fun bindView(sequenceStep: SequenceStep, item: T)
}
