const express = require("express");
const router = express.Router();
const {
  getAllHeroes,
  deleteHero,
  getDeletedHeroes,
  createHero,
  updateHero,
  getHeroById,
} = require("../controllers/charactersController");

router.get("/", getAllHeroes);
router.get("/:id", getHeroById);
router.get("/deleted", getDeletedHeroes);
router.delete("/:id", deleteHero);
router.put("/update/:id", updateHero);

module.exports = router;
