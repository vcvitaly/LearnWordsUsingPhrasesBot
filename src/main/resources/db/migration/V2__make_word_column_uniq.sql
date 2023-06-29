CREATE UNIQUE INDEX words_word_uniq_idx ON public.words (word);

ALTER INDEX IF EXISTS words_pkey RENAME TO words_pkey_idx;