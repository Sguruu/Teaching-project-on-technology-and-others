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
import androidx.lifecycle.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

/**
 * TitleRepository provides an interface to fetch a title or request a new one be generated.
 *
 * TitleRepository предоставляет интерфейс для получения заголовка или запроса создания нового.
 *
 * Repository modules handle data operations. They provide a clean API so that the rest of the app
 * can retrieve this data easily. They know where to get the data from and what API calls to make
 * when data is updated. You can consider repositories to be mediators between different data
 * sources, in our case it mediates between a network API and an offline database cache.
 *
 * Модули репозитория обрабатывают операции с данными. Они предоставляют чистый API, чтобы
 * остальная часть приложения могла легко извлекать эти данны. Они знают, откуда брат данные и
 * какие вызовы API делать при обновлении данных. Вы можете рассматривать репозитории как
 * посредников мужду различными источниками данных, в нашем случае это посредник между сетевыми API
 * и автономным кешем базы данных.
 */
class TitleRepository(val network: MainNetwork, val titleDao: TitleDao) {

    /**
     * [LiveData] to load title.
     *
     * [LiveData] для загрузки заголовка.
     *
     * This is the main interface for loading a title. The title will be loaded from the offline
     * cache.
     *
     * Это основной интерфейс для загрузки заголовка. Заголовк будет загружен из автономного кеша.
     *
     * Observing this will not cause the title to be refreshed, use [TitleRepository.refreshTitle]
     * to refresh the title.
     *
     * Если это не приведет к обновлению заголовка, используйте [TitleRepository.refreshTitle],
     * чтобы обновить заголовок.
     */
    val title: LiveData<String?> = titleDao.titleLiveData.map { it?.title }


    /**
     * Refresh the current title and save the results to the offline cache.
     *
     * Обновите текущий заголовок и сохраните результаты в автономном кеше.
     *
     * This method does not return the new title. Use [TitleRepository.title] to observe
     * the current tile.
     *
     * Этот метод не возвращает новый заголовк. Используйте [TitleRepository.title], чтобы
     * наблюдать за текущей плиткой.
     */
    suspend fun refreshTitle() {
        try {
            val result = withTimeout(5_000) {
                network.fetchNextTitle()
            }
            titleDao.insertTitle(Title(result))
        } catch (error: Throwable) {
            throw TitleRefreshError("Unable to refresh title", error)
        }
    }

    /**
     * This API is exposed for callers from the Java Programming language.
     *
     * Этот API предоставляется вызывающим абонентам с языка программирования Java.
     *
     * The request will run unstructured, which means it won't be able to be cancelled.
     * Запрос будет выполняться неструктурированным, а это значит, что его нельзя будет отменить.
     *
     * @param titleRefreshCallback a callback
     */
    fun refreshTitleInterop(titleRefreshCallback: TitleRefreshCallback) {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            try {
                refreshTitle()
                titleRefreshCallback.onCompleted()
            } catch (throwable: Throwable) {
                titleRefreshCallback.onError(throwable)
            }
        }
    }
}

/**
 * Thrown when there was a error fetching a new title
 *
 * Выдается, когда произошла ошибка при получении нового заголовка
 *
 * @property message user ready error message
 *
 * @property message сообщение об ошибке готовности пользователя
 *
 * @property cause the original cause of this exception
 *
 * @property cause первоначальная причина этого исключения
 *
 */
class TitleRefreshError(message: String, cause: Throwable) : Throwable(message, cause)

interface TitleRefreshCallback {
    fun onCompleted()
    fun onError(cause: Throwable)
}
