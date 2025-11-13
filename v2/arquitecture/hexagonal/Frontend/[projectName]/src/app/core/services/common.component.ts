import { Injectable } from "@angular/core";
import { PrimeNG } from "primeng/config";

@Injectable({
    providedIn: 'root'
})
export class CommonComponent {

    files = [];
    totalSize: number = 0;
    totalSizePercent: number = 0;

    constructor(private config: PrimeNG) {

    }
     
    choose(event: any, callback: () => void) {
        callback();
    }

    onRemoveTemplatingFile(event: any, file: { size: any; }, removeFileCallback: (arg0: any, arg1: any) => void, index: any) {
        removeFileCallback(event, index);
        this.totalSize -= parseInt(this.formatSize(file.size));
        this.totalSizePercent = this.totalSize / 10;
    }

    onClearTemplatingUpload(clear: () => void) {
        clear();
        this.totalSize = 0;
        this.totalSizePercent = 0;
    }

    onTemplatedUpload() {
        this.totalSizePercent = 70;
        
    }

    onSelectedFiles(event: any) {
        this.files = event.currentFiles;
        this.files.forEach((file) => {
            this.totalSize += parseInt(this.formatSize(file['size']));
        });
        this.totalSizePercent = this.totalSize / 10;
    }

    uploadEvent(callback: () => void) {
        callback();
    }

    formatSize(bytes: number) {
        const k = 1024;
        const dm = 3;
        let sizes: (String[] | undefined) = this.config.translation.fileSizeTypes;
        if (bytes === 0) {
            return '0' + (sizes ? sizes[0] : '');
        }

        const i = Math.floor(Math.log(bytes) / Math.log(k));
        const formattedSize = parseFloat((bytes / Math.pow(k, i)).toFixed(dm));

        return `${formattedSize}` + (sizes ? sizes[0] : '');
    }


}