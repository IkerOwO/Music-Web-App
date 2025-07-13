using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.Json;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Hosting;
using TagLib;

namespace MiAppMusica.Services
{
    // Alias para evitar conflicto entre System.IO.File y TagLib.File
    using SysFile = System.IO.File;
    using TagFile = TagLib.File;

    public class MusicService
    {
        private readonly IWebHostEnvironment _env;
        private readonly string _jsonFilePath;

        public List<AlbumInfo> Albums { get; private set; } = new();

        public MusicService(IWebHostEnvironment env)
        {
            _env = env;

            // Guarda el JSON en una carpeta Data dentro de la raíz del proyecto
            string dataFolder = Path.Combine(_env.ContentRootPath, "Data");
            if (!Directory.Exists(dataFolder))
                Directory.CreateDirectory(dataFolder);

            _jsonFilePath = Path.Combine(dataFolder, "musicData.json");
        }

        public void ScanSongs()
        {
            Albums.Clear();

            string folderPath = Path.Combine(_env.WebRootPath, "Songs");
            if (!Directory.Exists(folderPath))
                return;

            var audioFiles = Directory.GetFiles(folderPath)
                .Where(f => new[] { ".mp3", ".ogg", ".flac" }
                .Contains(Path.GetExtension(f).ToLowerInvariant()))
                .ToList();

            var albumDict = new Dictionary<string, AlbumInfo>();

            foreach (var file in audioFiles)
            {
                try
                {
                    var tfile = TagFile.Create(file);
                    string albumName = tfile.Tag.Album ?? "Desconocido";
                    string title = tfile.Tag.Title ?? Path.GetFileNameWithoutExtension(file);
                    string artist = tfile.Tag.FirstPerformer ?? "Desconocido";

                    string? base64Cover = null;
                    if (tfile.Tag.Pictures.Length > 0)
                    {
                        var pic = tfile.Tag.Pictures[0];
                        var bytes = pic.Data.Data;
                        base64Cover = $"data:image/jpeg;base64,{Convert.ToBase64String(bytes)}";
                    }

                    if (!albumDict.TryGetValue(albumName, out AlbumInfo? album))
                    {
                        album = new AlbumInfo
                        {
                            AlbumName = albumName,
                            CoverImageBase64 = base64Cover
                        };
                        albumDict.Add(albumName, album);
                    }
                    else if (album.CoverImageBase64 == null && base64Cover != null)
                    {
                        album.CoverImageBase64 = base64Cover;
                    }

                    album.Songs.Add(new SongInfo
                    {
                        FileName = Path.GetFileName(file),
                        Title = title,
                        Artist = artist
                    });
                }
                catch
                {
                    // Ignorar errores en archivos corruptos
                }
            }

            Albums = albumDict.Values.ToList();
        }

        public async Task SaveToJsonAsync()
        {
            var options = new JsonSerializerOptions { WriteIndented = true };
            string json = JsonSerializer.Serialize(Albums, options);
            await SysFile.WriteAllTextAsync(_jsonFilePath, json);
        }

        public async Task LoadFromJsonAsync()
        {
            if (!SysFile.Exists(_jsonFilePath))
            {
                Albums = new List<AlbumInfo>();
                return;
            }

            string json = await SysFile.ReadAllTextAsync(_jsonFilePath);
            Albums = JsonSerializer.Deserialize<List<AlbumInfo>>(json) ?? new List<AlbumInfo>();
        }
    }

    public class SongInfo
    {
        public string FileName { get; set; } = "";
        public string Title { get; set; } = "Desconocido";
        public string Artist { get; set; } = "Desconocido";
    }

    public class AlbumInfo
    {
        public string AlbumName { get; set; } = "Desconocido";
        public string? CoverImageBase64 { get; set; }
        public List<SongInfo> Songs { get; set; } = new();
    }
}
