package me.shiry_recode.starmail.sadlibrary.files.configurations;

public interface IFileWrapper {
    abstract String getName();
    abstract void perform() throws Exception;
}
