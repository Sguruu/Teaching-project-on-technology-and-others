/*
 * Copyright (C) 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.kotlincoroutines.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.kotlincoroutines.util.singleArgViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * MainViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 *
 * MainViewModel Разработана для хранения и управления данными, связанными с пользовательским
 * интерфейсом, с учетом жизненного цикла. Это позволяет данным сохранятся при повороте экрана.
 * Кроме того, фоновая работа, такая как получение сетвеых результатов, может продолжаться через
 * изменения конфигурации и предоставлять результаты после этого, как новый фрагмент или действие
 * станет доступным.
 *
 * @param repository the data source this ViewModel will fetch results from.
 *
 * @param repository репозиторий источника данных, из которого эта ViewModel будет получать
 * результаты.
 */
class MainViewModel(private val repository: TitleRepository) : ViewModel() {

    companion object {
        /**
         * Factory for creating [MainViewModel]
         *
         * Фабрика по созданию [MainViewModel]
         *
         * @param arg the repository to pass to [MainViewModel]
         *
         * @param arg репозиторий для передачи [MainViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::MainViewModel)
    }

    /**
     * Request a snackbar to display a string.
     *
     * Запросите snackbar чтобы получить строку
     *
     * This variable is private because we don't want to expose MutableLiveData
     *
     * Эта переменная является private, потому что мы не хотим раскрывать MutableLiveData
     *
     * MutableLiveData allows anyone to set a value, and MainViewModel is the only
     * class that should be setting values.
     *
     * MutableLiveData позволяет любому учтанавливать значение, а MainViewModel единственный класс,
     * который должен устанавливать значения.
     */
    private val _snackBar = MutableLiveData<String?>()

    /**
     * Request a snackbar to display a string.
     *
     * Запросите snackbar чтобы отобразить строку.
     */
    val snackbar: LiveData<String?>
        get() = _snackBar

    /**
     * Update title text via this LiveData
     *
     * Обновить текс заголовка через эту LiveData
     */
    val title = repository.title

    private val _spinner = MutableLiveData<Boolean>(false)

    /**
     * Show a loading spinner if true
     *
     * Показать счетчик загрузки если true
     */
    val spinner: LiveData<Boolean>
        get() = _spinner

    /**
     * Count of taps on the screen
     *
     * Количество нажатий на экран
     */
    private var tapCount = 0

    /**
     * LiveData with formatted tap count.
     *
     * LiveData с форматированным количеством нажатий
     */
    private val _taps = MutableLiveData<String>("$tapCount taps")

    /**
     * Public view of tap live data.
     *
     * Public просмотр live data
     */
    val taps: LiveData<String>
        get() = _taps

    /**
     * Respond to onClick events by refreshing the title.
     *
     * Отвечайте на событие onClick, обновляя заголовок.
     *
     * The loading spinner will display until a result is returned, and errors will trigger
     * a snackbar.
     *
     * Счетчик загрузки будет отображаться до тех пор, пока не будет запускаться snackbar
     *
     */
    fun onMainViewClicked() {
        refreshTitle()
        updateTaps()
    }

    private fun updateTaps() {
        viewModelScope.launch {
            delay(1_000)
            _taps.value = "${++tapCount} taps"
        }
    }

    /**
     * Called immediately after the UI shows the snackbar.
     *
     * Вызывается сразу после того, как в пользовательском интерфейсе отображается snackbar
     */
    fun onSnackbarShown() {
        _snackBar.value = null
    }

    /**
     * Refresh the title, showing a loading spinner while it refreshes and errors via snackbar.
     *
     * Обновите заголовок, показывая счетчик загрузки во время обновления и ошибок через snackbar
     */
    fun refreshTitle() = launchDataLoad {
        repository.refreshTitle()
    }

    /**
     * Helper function to call a data load function with a loading spinner, errors will trigger a
     * snackbar.
     *
     * Вспомогательная функция для вызова функции загрузки данных с помощью счетчика загрузки,
     * при ошибках будет запускаться snackbar.
     *
     * By marking `block` as `suspend` this creates a suspend lambda which can call suspend
     * functions.
     *
     * Пометка `block` как `suspend` создает лямбда приостановки, которая может вызвать функции
     * приостанвки корутины
     *
     * @param block lambda to actually load data. It is called in the viewModelScope. Before calling the
     *              lambda the loading spinner will display, after completion or error the loading
     *              spinner will stop\
     *
     * @param block лямбда до фактической загрузки данных. Он вызывается в viewModelScope. Перед
     *              вызовом лямбды будет отображаться счетчик загрузки, после завершения или ошибки
     *              счетчик загрузки остановится
     */
    private fun launchDataLoad(block: suspend () -> Unit): Unit {
        viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            } catch (error: TitleRefreshError) {
                _snackBar.value = error.message
            } finally {
                _spinner.value = false
            }
        }
    }
}
